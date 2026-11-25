package com.kelompoksix.siencehub.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SageSoft
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import com.kelompoksix.siencehub.utils.UserSessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfilScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val metrics = rememberResponsiveMetrics()

    val initialName = remember(context) { UserSessionManager.getUserName(context) }
    val initialPhone = remember(context) { UserSessionManager.getUserPhone(context) }
    val initialEmail = remember(context) { UserSessionManager.getUserEmail(context) }

    var nama by remember { mutableStateOf(initialName) }
    var nomorTelepon by remember { mutableStateOf(initialPhone) }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }

    val handleSave: () -> Unit = {
        val newName = nama.trim()
        val newPhone = nomorTelepon.trim()

        val isNameChanged = newName != initialName.trim()
        val isPhoneChanged = newPhone != initialPhone.trim()

        if (newName.isBlank()) {
            Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (!isNameChanged && !isPhoneChanged) {
            Toast.makeText(context, "Tidak ada perubahan data", Toast.LENGTH_SHORT).show()
        } else {
            isLoading = true

            when {
                isNameChanged && isPhoneChanged -> {
                    dialogTitle = "Profil Berhasil Diperbarui"
                    dialogMessage = "Nama Anda telah diubah menjadi \"$newName\" dan nomor HP diperbarui menjadi \"$newPhone\"."
                }
                isNameChanged -> {
                    dialogTitle = "Nama Berhasil Diperbarui"
                    dialogMessage = "Nama Anda telah berhasil diubah menjadi \"$newName\". Nama baru ini otomatis tampil di Beranda & Materi."
                }
                else -> {
                    dialogTitle = "Nomor HP Berhasil Diperbarui"
                    dialogMessage = "Nomor telepon Anda telah berhasil diperbarui menjadi \"$newPhone\"."
                }
            }

            UserSessionManager.updateProfile(context, newName, newPhone)

            val firebaseUser = FirebaseAuth.getInstance().currentUser
            if (firebaseUser != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build()
                firebaseUser.updateProfile(profileUpdates)

                try {
                    val db = FirebaseFirestore.getInstance()
                    val userDocData = hashMapOf(
                        "uid" to firebaseUser.uid,
                        "email" to (firebaseUser.email ?: initialEmail),
                        "displayName" to newName,
                        "phone" to newPhone,
                        "lastUpdated" to System.currentTimeMillis()
                    )
                    db.collection("users").document(firebaseUser.uid)
                        .set(userDocData, com.google.firebase.firestore.SetOptions.merge())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            isLoading = false
            showSuccessDialog = true
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(SageSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = SagePrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    text = dialogTitle,
                    fontSize = 18.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = dialogMessage,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onBackClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text("Selesai", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = SurfaceWhite,
            tonalElevation = 6.dp
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SagePrimary)
            .imePadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = metrics.horizontalPadding, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = onBackClick,
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Kembali",
                        tint = SagePrimary,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Edit Profil",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = SurfaceOffWhite,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            ResponsiveContentContainer(maxWidth = 520.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .border(3.dp, SagePrimary, CircleShape)
                            .clip(CircleShape)
                            .background(SageSoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = SagePrimary,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it },
                        label = { Text("Nama Lengkap") },
                        leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, tint = SagePrimary) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SagePrimary,
                            unfocusedBorderColor = BorderSubtle,
                            focusedLabelColor = SagePrimary,
                            unfocusedLabelColor = TextSecondary,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = SurfaceWhite,
                            unfocusedContainerColor = SurfaceWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = nomorTelepon,
                        onValueChange = { nomorTelepon = it },
                        label = { Text("Nomor Telepon") },
                        placeholder = { Text("0812xxxxxxxx") },
                        leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null, tint = SagePrimary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SagePrimary,
                            unfocusedBorderColor = BorderSubtle,
                            focusedLabelColor = SagePrimary,
                            unfocusedLabelColor = TextSecondary,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = SurfaceWhite,
                            unfocusedContainerColor = SurfaceWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = initialEmail.ifBlank { "Tidak terdaftar" },
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        shape = RoundedCornerShape(14.dp),
                        label = { Text("Email (Tidak dapat diubah)") },
                        leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = TextSecondary) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = handleSave,
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Simpan Perubahan",
                                color = Color.White,
                                fontSize = 15.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
