package com.kelompoksix.siencehub.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.StatusDanger
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
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

    val context = LocalContext.current
    val metrics = rememberResponsiveMetrics()

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
            if (e.statusCode != 12501) {
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
                        if (phone.isNotBlank()) {
                            UserSessionManager.updateProfile(context, UserSessionManager.getUserName(context), phone.trim())
                        }
                        Toast.makeText(context, "Akun berhasil dibuat! Selamat datang di ScienceHub.", Toast.LENGTH_SHORT).show()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
            .systemBarsPadding()
            .imePadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = onNavigateToLogin,
                shape = CircleShape,
                color = SurfaceOffWhite,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        ResponsiveContentContainer(
            maxWidth = metrics.formMaxWidth,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign up",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Buat akun baru untuk mulai menjelajahi sains",
                    fontSize = 13.5.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(28.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email address") },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp)) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SagePrimary,
                        unfocusedBorderColor = BorderSubtle,
                        focusedLabelColor = SagePrimary,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceOffWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone number") },
                    leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp)) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SagePrimary,
                        unfocusedBorderColor = BorderSubtle,
                        focusedLabelColor = SagePrimary,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceOffWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp)) },
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SagePrimary,
                        unfocusedBorderColor = BorderSubtle,
                        focusedLabelColor = SagePrimary,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceOffWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm password") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp)) },
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(image, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SagePrimary,
                        unfocusedBorderColor = BorderSubtle,
                        focusedLabelColor = SagePrimary,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceOffWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (!errorMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        color = Color(0xFFFEF2F2),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFFECACA)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = StatusDanger,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = handleSignup,
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
                        Text("Create Account", color = Color.White, fontSize = 15.5.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = BorderSubtle)
                    Text(
                        text = "  atau  ",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = BorderSubtle)
                }

                Spacer(modifier = Modifier.height(18.dp))

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
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, BorderSubtle),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceWhite)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "G",
                            fontWeight = FontWeight.Black,
                            fontSize = 19.sp,
                            color = Color(0xFF4285F4)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Daftar dengan Google",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceOffWhite)
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = TextSecondary)) { append("Already have an account? ") }
                    withStyle(style = SpanStyle(color = SagePrimary, fontWeight = FontWeight.Bold)) { append("Login") }
                },
                fontSize = 13.5.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }
}
