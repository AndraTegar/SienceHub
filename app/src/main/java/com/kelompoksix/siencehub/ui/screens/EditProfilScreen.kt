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
import com.kelompoksix.siencehub.utils.UserSessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfilScreen(
    onBackClick: () -> Unit
) {
    val sageGreen = Color(0xFF7A8B76)
    val context = LocalContext.current

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

            // Tentukan Judul & Pesan Pop-up secara spesifik
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

            // 1. Simpan ke Local Preferences (instan)
            UserSessionManager.updateProfile(context, newName, newPhone)

            // 2. Simpan ke Firebase Auth & Cloud Firestore di background
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

    // ==========================================
    // POPUP DIALOG MINIMALIS & CLEAN
    // ==========================================
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8ECE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = sageGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    text = dialogTitle,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = dialogMessage,
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563),
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
                    colors = ButtonDefaults.buttonColors(containerColor = sageGreen),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text("Selesai", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White,
            tonalElevation = 6.dp
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(sageGreen)
    ) {
        // ==========================================
        // HEADER ATAS (Tombol Back & Judul)
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(50))
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Kembali",
                    tint = sageGreen,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Edit Profil",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // ==========================================
        // KONTEN UTAMA (Kartu Putih Melengkung)
        // ==========================================
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFAFAFA),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Avatar Pengguna
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(3.dp, sageGreen, CircleShape)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Input Nama Lengkap
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Lengkap") },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, tint = sageGreen) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = sageGreen,
                        focusedLabelColor = sageGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input Nomor Telepon
                OutlinedTextField(
                    value = nomorTelepon,
                    onValueChange = { nomorTelepon = it },
                    label = { Text("Nomor Telepon") },
                    placeholder = { Text("0812xxxxxxxx") },
                    leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null, tint = sageGreen) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = sageGreen,
                        focusedLabelColor = sageGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Informasi Email (Disabled / Readonly)
                OutlinedTextField(
                    value = initialEmail.ifBlank { "Tidakar terdaftar" },
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Email (Tidakar dapat diubah)") },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = Color.Gray) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Tombol Simpan Perubahan
                Button(
                    onClick = handleSave,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = sageGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Simpan Perubahan",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
