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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.R

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

    val primaryGreen = Color(0xFF7A8B76)
    val textGreen = Color(0xFF778873)
    val focusManager = LocalFocusManager.current

    // Validasi: Aktif jika semua terisi DAN password cocok dengan confirm password
    val isFormValid = email.isNotEmpty() &&
            phone.isNotEmpty() &&
            password.isNotEmpty() &&
            confirmPassword.isNotEmpty() &&
            (password == confirmPassword)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .imePadding()
    ) {
        // 1. Top Bar (Tombol Back)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateToLogin) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }

        // 2. Konten Utama (Scrollable)
        Column(
            modifier = Modifier
                .weight(1f) // Mendorong footer ke bawah
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sign up",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textGreen,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Email Field
            Text(text = "Email", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textGreen)
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Enter your email", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = textGreen,
                    unfocusedIndicatorColor = Color.LightGray,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Phone Field
            Text(text = "Phone number", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textGreen)
            TextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = { Text("Enter your phone number", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = textGreen,
                    unfocusedIndicatorColor = Color.LightGray,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Password Field
            Text(text = "Password", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textGreen)
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Create a password", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(id = icon), contentDescription = null, tint = Color.Gray)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = textGreen,
                    unfocusedIndicatorColor = Color.LightGray,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Confirm Password Field
            Text(text = "Confirm password", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textGreen)
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Repeat your password", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (confirmPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(painter = painterResource(id = icon), contentDescription = null, tint = Color.Gray)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (isFormValid) {
                            onCreateAccountSuccess()
                        }
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = textGreen,
                    unfocusedIndicatorColor = Color.LightGray,
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Tombol Sign Up
            Button(
                onClick = onCreateAccountSuccess,
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
                Text("Sign up", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // 3. Footer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8F8F8))
                .padding(vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text("Already have an account? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Log in",
                    color = primaryGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}