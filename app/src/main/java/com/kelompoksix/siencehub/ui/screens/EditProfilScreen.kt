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
import androidx.compose.material.icons.filled.CheckCircle
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
    var updatedNameResult by remember { mutableStateOf("") }
    var updatedPhoneResult by remember { mutableStateOf("") }

    val handleSave: () -> Unit = {
        val newName = nama.trim()
        val newPhone = nomorTelepon.trim()

        if (newName.isBlank()) {
            Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {
            isLoading = true
            updatedNameResult = newName
            updatedPhoneResult = newPhone

            // 1. Simpan langsung ke Local Preferences (instan)
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

            // 3. Tampilkan Toast & Pop-up Sukses Langsung
            isLoading = false
            Toast.makeText(context, "Profil & Nama Berhasil Diperbarui!", Toast.LENGTH_LONG).show()
            showSuccessDialog = true
        }
    }

    // ==========================================
    // POPUP DIALOG BERHASIL MENGUBAH PROFIL
    // ==========================================
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Berhasil",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(54.dp)
                )
            },
            title = {
                Text(
                    text = "Profil Berhasil Diperbarui! 🎉",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                val phoneText = if (updatedPhoneResult.isNotBlank()) "\nNomor HP: $updatedPhoneResult" else ""
                Text(
                    text = "Nama Anda telah diperbarui menjadi \"$updatedNameResult\"$phoneText.\n\nPerubahan ini otomatis tampil di halaman Beranda, Profil, dan Modul Materi.",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onBackClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = sageGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("OK, Mengerti", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
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
