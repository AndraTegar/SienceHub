package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.R
import com.kelompoksix.siencehub.ui.components.WavyShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    val primaryGreen = Color(0xFF7A8B76)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FC)) // Background dasar layar
            .imePadding()
    ) {

        // 1. Gambar Latar Belakang Hijau Bergelombang (Diam di tempat)
        Image(
            painter = painterResource(id = R.drawable.texture_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop, // Gunakan FillBounds jika gambar kurang lebar
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .clip(WavyShape())
        )

        // 2. Area Scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            // Spacer transparan (tanpa background) agar gambar hijau terlihat
            // Tingginya bisa Anda sesuaikan agar pas dengan batas lengkungan
            Spacer(modifier = Modifier.height(290.dp))

            // 3. KONTEN FORM DENGAN BACKGROUND PUTIH (Ini yang akan ikut naik)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9F9FC)) // Kunci perbaikannya ada di sini
                    .padding(horizontal = 32.dp)
                    .padding(top = 24.dp) // Jarak tambahan di atas tulisan "Sign in"
            ) {

                // Judul "Sign in"
                Column {
                    Text(
                        text = "Sign in",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .width(48.dp)
                            .height(3.dp)
                            .background(primaryGreen)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Field Email
                Text(text = "Email", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("demo@email.com", color = Color.Gray) },
                    leadingIcon = {
                        Icon(painterResource(id = R.drawable.ic_email), contentDescription = null, tint = Color.Gray)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = primaryGreen,
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Field Password
                Text(text = "Password", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("enter your password", color = Color.Gray) },
                    leadingIcon = {
                        Icon(painterResource(id = R.drawable.ic_lock), contentDescription = null, tint = Color.Gray)
                    },
                    trailingIcon = {
                        val image = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(painterResource(id = image), contentDescription = null, tint = Color.Gray)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = primaryGreen,
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Remember Me & Forgot Password
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(checkedColor = primaryGreen)
                        )
                        Text("Remember Me", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Text(
                        text = "Forgot Password?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen,
                        modifier = Modifier.clickable { /* Aksi lupa password */ }
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Tombol Login
                Button(
                    onClick = { /* Aksi Login */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Teks Sign Up
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Don't have an Account ? ", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = "Sign up",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen,
                        modifier = Modifier.clickable { /* Aksi Sign up */ }
                    )
                }

                Spacer(modifier = Modifier.height(48.dp)) // Jarak tambahan di bagian paling bawah
            }
        }
    }
}