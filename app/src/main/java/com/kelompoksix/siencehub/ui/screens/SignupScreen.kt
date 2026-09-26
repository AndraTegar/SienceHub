package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    onCreateAccountSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val sageGreen = Color(0xFF7A8B76)
    val darkText = Color(0xFF161823)
    val fieldBgColor = Color(0xFFF1F2F4) // Warna abu-abu kartu modern

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // ==========================================
            // TOP BAR (Tombol Close / Silang)
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateToLogin) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = darkText,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // HEADER (Judul "Sign up" di Tengah)
            // ==========================================
            Text(
                text = "Sign up",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = darkText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ==========================================
            // FORM INPUT (Model Kartu Modern)
            // ==========================================

            // Email
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email address", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp)) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = fieldBgColor,
                    unfocusedContainerColor = fieldBgColor,
                    disabledContainerColor = fieldBgColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Phone no
            TextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = { Text("Phone number", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp)) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = fieldBgColor,
                    unfocusedContainerColor = fieldBgColor,
                    disabledContainerColor = fieldBgColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp)) },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = fieldBgColor,
                    unfocusedContainerColor = fieldBgColor,
                    disabledContainerColor = fieldBgColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm Password
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Confirm password", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp)) },
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(image, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = fieldBgColor,
                    unfocusedContainerColor = fieldBgColor,
                    disabledContainerColor = fieldBgColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Create Account
            Button(
                onClick = onCreateAccountSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = sageGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Create Account", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // Spacer pendorong agar teks berada di posisi paling bawah layar
            Spacer(modifier = Modifier.height(60.dp))

            // ==========================================
            // FOOTER (Sudah Punya Akun? Login di Bawah)
            // ==========================================
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) { append("Already have an account? ") }
                    withStyle(style = SpanStyle(color = sageGreen, fontWeight = FontWeight.Bold)) { append("Login") }
                },
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clickable { onNavigateToLogin() }
            )
        }
    }
}