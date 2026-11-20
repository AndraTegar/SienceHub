package com.kelompoksix.siencehub.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kelompoksix.siencehub.utils.UserSessionManager

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

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val sageGreen = Color(0xFF7A8B76)
    val darkText = Color(0xFF161823)
    val fieldBgColor = Color(0xFFF1F2F4) // Warna abu-abu kartu modern
    val context = LocalContext.current

    val webClientId = remember(context) {
        context.getString(com.kelompoksix.siencehub.R.string.default_web_client_id)
    }

    val gso = remember(context, webClientId) {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember(context, gso) { GoogleSignIn.getClient(context, gso) }

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null && account.idToken != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                isLoading = true
                errorMessage = null
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnSuccessListener { authResult ->
                        isLoading = false
                        val userEmail = authResult.user?.email ?: account.email ?: ""
                        val userName = authResult.user?.displayName ?: account.displayName
                        UserSessionManager.saveSession(context, userEmail, userName)
                        Toast.makeText(context, "Daftar Google Berhasil! Selamat Datang.", Toast.LENGTH_SHORT).show()
                        onCreateAccountSuccess()
                    }
                    .addOnFailureListener { exception ->
                        isLoading = false
                        errorMessage = "Gagal pendaftaran Google: ${exception.localizedMessage}"
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
            }
        } catch (e: ApiException) {
            isLoading = false
            if (e.statusCode != 12501) { // 12501 = Canceled
                errorMessage = "Gagal login Google (Code: ${e.statusCode})"
            }
        }
    }

    val handleSignup: () -> Unit = {
        val cleanEmail = email.trim()
        when {
            cleanEmail.isBlank() -> errorMessage = "Email tidak boleh kosong."
            !cleanEmail.contains("@") -> errorMessage = "Format email tidak valid."
            password.length < 6 -> errorMessage = "Password minimal terdiri dari 6 karakter."
            password != confirmPassword -> errorMessage = "Konfirmasi password tidak cocok dengan password."
            else -> {
                isLoading = true
                errorMessage = null
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(cleanEmail, password)
                    .addOnSuccessListener {
                        isLoading = false
                        UserSessionManager.saveSession(context, cleanEmail)
                        Toast.makeText(context, "Akun berhasil dibuat! Selamat datang di SienceHub.", Toast.LENGTH_SHORT).show()
                        onCreateAccountSuccess()
                    }
                    .addOnFailureListener { exception ->
                        isLoading = false
                        val msg = when {
                            exception.message?.contains("already in use", ignoreCase = true) == true -> "Email sudah terdaftar. Silakan gunakan email lain atau login."
                            exception.message?.contains("badly formatted", ignoreCase = true) == true -> "Format email tidak valid."
                            else -> exception.localizedMessage ?: "Pendaftaran gagal. Periksa kembali data Anda."
                        }
                        errorMessage = msg
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

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
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
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
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
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
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
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
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = fieldBgColor,
                    unfocusedContainerColor = fieldBgColor,
                    disabledContainerColor = fieldBgColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Create Account
            Button(
                onClick = handleSignup,
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
                    Text("Create Account", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // TOMBOL SIGN UP DENGAN GOOGLE
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                Text(
                    text = "  atau  ",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    googleSignInClient.signOut().addOnCompleteListener {
                        googleLauncher.launch(googleSignInClient.signInIntent)
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFDADCE0)),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "G",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = Color(0xFF4285F4)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Daftar dengan Google",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3C4043)
                    )
                }
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
