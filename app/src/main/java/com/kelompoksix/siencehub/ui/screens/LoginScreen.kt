package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.R

class WavyShapeLogin : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            lineTo(0f, size.height * 0.75f)
            cubicTo(
                size.width * 0.25f, size.height * 0.95f,
                size.width * 0.75f, size.height * 0.65f,
                size.width, size.height * 0.85f
            )
            lineTo(size.width, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onNavigateToSignup: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val sageGreen = Color(0xFF7A8B76)
    val darkText = Color(0xFF333333)

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // Background Gambar Wavy
        Image(
            painter = painterResource(id = R.drawable.texture_bg),
            contentDescription = "Background Wavy",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.48f)
                .clip(WavyShapeLogin()),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )

        // Menggunakan verticalScroll dan imePadding agar aman saat keyboard muncul
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding() // Menyesuaikan otomatis saat keyboard HP muncul
                .padding(horizontal = 32.dp)
        ) {
            // Jarak atas dikurangi (140.dp) agar teks "Log in" naik masuk ke area hijau
            Spacer(modifier = Modifier.height(140.dp))

            // Header Form (Log in)
            Column {
                Text(
                    text = "Log in",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Masukkan akun Anda untuk melanjutkan",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .height(3.dp)
                        .width(50.dp)
                        .background(sageGreen, shape = RoundedCornerShape(2.dp))
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Input Email
            Text("Email", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = darkText)
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("demo@email.com", fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, modifier = Modifier.size(18.dp)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = sageGreen,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Input Password
            Text("Password", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = darkText)
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("enter your password", fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, modifier = Modifier.size(18.dp)) },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = sageGreen,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

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
                        colors = CheckboxDefaults.colors(checkedColor = sageGreen)
                    )
                    Text("Remember Me", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Text("Forgot Password?", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = darkText)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Tombol Login
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = sageGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Login", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Teks Navigasi ke Signup
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) { append("Don't have an Account ? ") }
                    withStyle(style = SpanStyle(color = sageGreen, fontWeight = FontWeight.Bold)) { append("Sign up") }
                },
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 32.dp)
                    .clickable { onNavigateToSignup() }
            )
        }
    }
}