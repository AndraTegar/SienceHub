package com.kelompoksix.siencehub.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.kelompoksix.siencehub.utils.UserSessionManager

@Composable
fun EditProfilDialog(
    onDismissRequest: () -> Unit,
    onProfileUpdated: () -> Unit
) {
    val context = LocalContext.current
    val currentName = remember(context) { UserSessionManager.getUserName(context) }
    val currentPhone = remember(context) { UserSessionManager.getUserPhone(context) }

    var nama by remember { mutableStateOf(currentName) }
    var nomorTelepon by remember { mutableStateOf(currentPhone) }
    var isLoading by remember { mutableStateOf(false) }

    val sageGreen = Color(0xFF7A8B76)

    val handleSaveProfile: () -> Unit = {
        val newName = nama.trim()
        val newPhone = nomorTelepon.trim()

        if (newName.isBlank()) {
            Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {
            isLoading = true
            val firebaseUser = FirebaseAuth.getInstance().currentUser

            // 1. Simpan langsung ke Local Preferences
            UserSessionManager.updateProfile(context, newName, newPhone)

            // 2. Simpan ke Firebase Auth & Cloud Firestore
            if (firebaseUser != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build()
                firebaseUser.updateProfile(profileUpdates)

                try {
                    val db = FirebaseFirestore.getInstance()
                    val userDocData = hashMapOf(
                        "uid" to firebaseUser.uid,
                        "email" to (firebaseUser.email ?: ""),
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

            // 3. Tampilkan Toast Notifikasi Pemberitahuan Sukses
            isLoading = false
            Toast.makeText(context, "Profil & Nama Berhasil Diperbarui!", Toast.LENGTH_LONG).show()
            onProfileUpdated()
            onDismissRequest()
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Edit Profil",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Input Nama
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

                Spacer(modifier = Modifier.height(14.dp))

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

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        enabled = !isLoading
                    ) {
                        Text("Batal", color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = handleSaveProfile,
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = sageGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Simpan", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
