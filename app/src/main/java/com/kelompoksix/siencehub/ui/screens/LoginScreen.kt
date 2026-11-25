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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
fun LoginScreen(
    onBackClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onNavigateToSignup: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showForgotDialog by remember { mutableStateOf(false) }
    var resetEmailInput by remember { mutableStateOf("") }

    val isFormValid = email.isNotBlank() && password.isNotBlank() && !isLoading
    val focusManager = LocalFocusManager.current
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
    val googleSignInClient = remember(context) { GoogleSignIn.getClient(context, gso) }

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
                        Toast.makeText(context, "Login Google Berhasil! Selamat Datang.", Toast.LENGTH_SHORT).show()
                        onLoginSuccess()
                    }
                    .addOnFailureListener { exception ->
                        isLoading = false
                        errorMessage = "Gagal login dengan Google: ${exception.localizedMessage}"
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

    val handleLogin = {
        if (isFormValid) {
            focusManager.clearFocus()
            isLoading = true
            errorMessage = null

            val cleanEmail = email.trim()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(cleanEmail, password)
                .addOnSuccessListener { authResult ->
                    isLoading = false
                    val userEmail = authResult.user?.email ?: cleanEmail
                    val userName = authResult.user?.displayName
                    UserSessionManager.saveSession(context, userEmail, userName)
                    Toast.makeText(context, "Login Berhasil! Selamat Datang.", Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                }
                .addOnFailureListener { exception ->
                    isLoading = false
                    val msg = when {
                        exception.message?.contains("no user record", ignoreCase = true) == true -> "Akun tidak ditemukan. Silakan daftar terlebih dahulu."
                        exception.message?.contains("invalid", ignoreCase = true) == true -> "Email atau password salah."
                        exception.message?.contains("network", ignoreCase = true) == true -> "Gagal terhubung ke jaringan internet."
                        else -> exception.localizedMessage ?: "Gagal melakukan login. Periksa kembali email dan password Anda."
                    }
                    errorMessage = msg
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
        }
    }

    if (showForgotDialog) {
        AlertDialog(
            onDismissRequest = { showForgotDialog = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text("Reset Kata Sandi", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
            },
            text = {
                Column {
                    Text(
                        "Masukkan email akunmu untuk menerima tautan pengaturan ulang kata sandi.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = resetEmailInput,
                        onValueChange = { resetEmailInput = it },
                        label = { Text("Email") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val targetEmail = resetEmailInput.trim()
                        if (targetEmail.isBlank() || !targetEmail.contains("@")) {
                            Toast.makeText(context, "Masukkan alamat email yang valid.", Toast.LENGTH_SHORT).show()
                        } else {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(targetEmail)
                                .addOnSuccessListener {
                                    showForgotDialog = false
                                    Toast.makeText(
                                        context,
                                        "Link reset kata sandi telah dikirim ke $targetEmail.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                .addOnFailureListener { err ->
                                    Toast.makeText(
                                        context,
                                        err.localizedMessage ?: "Gagal mengirim email reset.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Kirim Link", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotDialog = false }) {
                    Text("Batal", color = TextSecondary)
                }
            }
        )
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
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = onBackClick,
                shape = CircleShape,
                color = SurfaceOffWhite,
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
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
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Log in",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SagePrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Masuk untuk melanjutkan progres belajarmu",
                    fontSize = 13.5.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email atau Username") },
                    placeholder = { Text("nama@email.com") },
                    leadingIcon = {
                        Icon(Icons.Outlined.Email, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SagePrimary,
                        unfocusedBorderColor = BorderSubtle,
                        focusedLabelColor = SagePrimary,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceOffWhite
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    placeholder = { Text("Masukkan password") },
                    leadingIcon = {
                        Icon(Icons.Outlined.Lock, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                    },
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = null, tint = TextSecondary)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            handleLogin()
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SagePrimary,
                        unfocusedBorderColor = BorderSubtle,
                        focusedLabelColor = SagePrimary,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceOffWhite
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Lupa kata sandi?",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SagePrimary,
                        modifier = Modifier.clickable(onClick = {
                            resetEmailInput = email.trim()
                            showForgotDialog = true
                        })
                    )
                }

                if (!errorMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
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
                    onClick = handleLogin,
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SagePrimary,
                        disabledContainerColor = Color(0xFFECEEF0),
                        contentColor = Color.White,
                        disabledContentColor = Color(0xFFA0AAB4)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Log in", fontSize = 15.5.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

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

                Spacer(modifier = Modifier.height(20.dp))

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
                            text = "Lanjutkan dengan Google",
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account? ", color = TextSecondary, fontSize = 13.5.sp)
                Text(
                    text = "Sign up",
                    color = SagePrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    modifier = Modifier.clickable(onClick = onNavigateToSignup)
                )
            }
        }
    }
}
