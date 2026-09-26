package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onBackClick: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val primaryGreen = Color(0xFF7A8B76)

    // Logika ala TikTok: Tombol login hanya aktif menyala jika kedua kolom sudah diisi
    val isFormValid = email.isNotEmpty() && password.isNotEmpty()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding() // Menjaga UI dari status bar (atas) dan nav bar (bawah)
            .imePadding() // PENTING: Mendorong seluruh Column ke atas saat keyboard muncul
    ) {
        // 1. Top Bar (Tombol Back)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                // Pastikan Anda sudah menambahkan ic_arrow_back di drawable
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }

        // 2. Konten Utama (Berada di tengah dan bisa di-scroll)
        Column(
            modifier = Modifier
                .weight(1f) // Mendorong Footer agar selalu berada di paling bawah
                .verticalScroll(rememberScrollState()) // Form bisa discroll saat ruang menyempit
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Log in",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF778873),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Email or username",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF778873)
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true, // Mencegah enter membuat baris baru ke bawah
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next // Mengubah tombol enter menjadi tombol "Next"
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFF778873),
                    unfocusedIndicatorColor = Color.LightGray,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = Color.LightGray) },
                trailingIcon = {
                    val icon = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(id = icon), contentDescription = null, tint = Color.Gray)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true, // Mencegah enter membuat baris baru
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done // Mengubah tombol enter menjadi "Done/Centang"
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus() // Menutup keyboard saat Done ditekan
                        if (isFormValid) {
                            // Masukkan aksi login ke sini nanti
                        }
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFF778873),
                    unfocusedIndicatorColor = Color.LightGray,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Forgot password?",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.clickable { /* Aksi lupa password */ }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* Aksi Login */ },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryGreen,
                    disabledContainerColor = Color(0xFFF1F1F2),
                    contentColor = Color.White,
                    disabledContentColor = Color(0xFFB0B0B4)
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Log in", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // Memberikan sedikit ruang di bawah tombol agar tidak mepet dengan Footer saat discroll
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 3. Footer (Menempel secara absolut di bawah)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8F8F8))
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text("Don't have an account? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Sign up",
                    color = primaryGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { /* Aksi Sign up */ }
                )
            }
        }
    }
}