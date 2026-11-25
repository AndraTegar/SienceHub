package com.kelompoksix.siencehub.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.kelompoksix.siencehub.data.repositories.LocalLearningRepository
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SageSoft
import com.kelompoksix.siencehub.ui.theme.StatusDanger
import com.kelompoksix.siencehub.ui.theme.StatusSuccess
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import com.kelompoksix.siencehub.utils.AppSettingsManager
import com.kelompoksix.siencehub.utils.UserSessionManager

private enum class InfoDialogType {
    ABOUT,
    PRIVACY,
    TERMS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onNavigateToEditProfile: () -> Unit = {}
) {
    val context = LocalContext.current
    val metrics = rememberResponsiveMetrics()

    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showResetProgressDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var activeInfoDialog by remember { mutableStateOf<InfoDialogType?>(null) }

    val isDarkMode = AppSettingsManager.isDarkMode
    val isNotificationsEnabled = AppSettingsManager.isNotificationsEnabled

    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showChangePasswordDialog = false }
        )
    }

    if (showResetProgressDialog) {
        AlertDialog(
            onDismissRequest = { showResetProgressDialog = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = "Reset Progres Belajar?",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Tindakan ini akan mengosongkan catatan bab selesai, XP lokal, dan streak belajar di perangkat ini agar kamu dapat mengulang dari awal.",
                    color = TextSecondary,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        LocalLearningRepository.clearUserLocalProgress(context)
                        showResetProgressDialog = false
                        Toast.makeText(context, "Progres belajar lokal berhasil direset.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusDanger),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Reset", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetProgressDialog = false }) {
                    Text("Batal", color = TextSecondary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = "Keluar dari Akun?",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Progres yang telah tersinkronisasi ke akunmu tetap aman. Kamu perlu login kembali untuk mengakses SienceHub.",
                    color = TextSecondary,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusDanger),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Keluar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal", color = TextSecondary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    activeInfoDialog?.let { infoType ->
        val (title, body) = when (infoType) {
            InfoDialogType.ABOUT -> Pair(
                "Tentang SienceHub",
                "SienceHub v1.0 adalah platform edukasi sains interaktif karya Kelompok Six.\n\n" +
                    "Dilengkapi 6 mata pelajaran (Fisika, Biologi, Kimia, Astronomi, Matematika, dan Geografi), " +
                    "laboratorium eksperimen virtual real-time, kuis evaluasi berwaktu, serta sistem gamifikasi Level, XP, Streak, dan Leaderboard."
            )
            InfoDialogType.PRIVACY -> Pair(
                "Kebijakan Privasi",
                "• Informasi akun (nama, email, nomor telepon) digunakan semata-mata untuk identifikasi profil dan sinkronisasi progres belajar.\n\n" +
                    "• Data progres bab, riwayat kuis, dan XP disimpan secara aman di perangkat lokal serta Cloud Firestore.\n\n" +
                    "• SienceHub tidak membagikan atau menjual data pribadi pengguna kepada pihak ketiga mana pun."
            )
            InfoDialogType.TERMS -> Pair(
                "Syarat & Ketentuan",
                "• Aplikasi SienceHub disediakan sebagai sarana pembelajaran edukatif bagi pelajar.\n\n" +
                    "• Pengguna bertanggung jawab menjaga kerahasiaan kata sandi akun masing-masing.\n\n" +
                    "• Skor pada Leaderboard diperoleh secara jujur melalui penyelesaian bab materi dan evaluasi kuis."
            )
        }

        AlertDialog(
            onDismissRequest = { activeInfoDialog = null },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = body,
                    color = TextSecondary,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { activeInfoDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Mengerti", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SagePrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = metrics.horizontalPadding, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = onBackClick,
                shape = CircleShape,
                color = SurfaceWhite,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Kembali",
                        tint = SagePrimary,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Pengaturan",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = SurfaceOffWhite,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            ResponsiveContentContainer(maxWidth = 640.dp) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                    contentPadding = PaddingValues(
                        horizontal = metrics.horizontalPadding,
                        vertical = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // 1. PENGATURAN AKUN
                    item {
                        SectionHeader("PENGATURAN AKUN")
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                            border = BorderStroke(1.dp, BorderSubtle),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column {
                                SettingItemAction(
                                    title = "Edit Profil",
                                    subtitle = "Ubah nama tampilan dan nomor telepon",
                                    leadingIcon = Icons.Default.Person,
                                    onClick = onNavigateToEditProfile
                                )
                                HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(horizontal = 16.dp))
                                SettingItemAction(
                                    title = "Ubah Kata Sandi",
                                    subtitle = "Perbarui password akun atau kirim link reset",
                                    leadingIcon = Icons.Default.Lock,
                                    onClick = { showChangePasswordDialog = true }
                                )
                            }
                        }
                    }

                    // 2. TAMPILAN & PREFERENSI
                    item {
                        SectionHeader("TAMPILAN & PREFERENSI")
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                            border = BorderStroke(1.dp, BorderSubtle),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column {
                                SettingItemToggle(
                                    title = "Mode Gelap",
                                    subtitle = if (isDarkMode) "Tampilan gelap aktif (nyaman di mata)" else "Tampilan terang aktif",
                                    leadingIcon = Icons.Default.DarkMode,
                                    checked = isDarkMode,
                                    onCheckedChange = { AppSettingsManager.setDarkMode(context, it) }
                                )
                                HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(horizontal = 16.dp))
                                SettingItemToggle(
                                    title = "Notifikasi Belajar",
                                    subtitle = "Info kenaikan level, streak, & pencapaian",
                                    leadingIcon = Icons.Default.Notifications,
                                    checked = isNotificationsEnabled,
                                    onCheckedChange = { AppSettingsManager.setNotificationsEnabled(context, it) }
                                )
                                HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(horizontal = 16.dp))
                                SettingItemAction(
                                    title = "Reset Progres Lokal",
                                    subtitle = "Ulangi progres bab & latihan di perangkat ini",
                                    leadingIcon = Icons.Default.DeleteSweep,
                                    iconTint = StatusDanger,
                                    onClick = { showResetProgressDialog = true }
                                )
                            }
                        }
                    }

                    // 3. INFORMASI LAINNYA
                    item {
                        SectionHeader("INFORMASI & BANTUAN")
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                            border = BorderStroke(1.dp, BorderSubtle),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column {
                                SettingItemAction(
                                    title = "Tentang SienceHub",
                                    subtitle = "Versi 1.0 • Kelompok Six",
                                    leadingIcon = Icons.Default.Info,
                                    onClick = { activeInfoDialog = InfoDialogType.ABOUT }
                                )
                                HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(horizontal = 16.dp))
                                SettingItemAction(
                                    title = "Kebijakan Privasi",
                                    leadingIcon = Icons.Default.PrivacyTip,
                                    onClick = { activeInfoDialog = InfoDialogType.PRIVACY }
                                )
                                HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(horizontal = 16.dp))
                                SettingItemAction(
                                    title = "Syarat & Ketentuan",
                                    leadingIcon = Icons.Default.Description,
                                    onClick = { activeInfoDialog = InfoDialogType.TERMS }
                                )
                            }
                        }
                    }

                    // 4. TOMBOL LOG OUT
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = { showLogoutDialog = true },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, StatusDanger.copy(alpha = 0.4f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = StatusDanger.copy(alpha = 0.1f),
                                contentColor = StatusDanger
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null,
                                modifier = Modifier.size(19.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Keluar Akun",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        color = TextSecondary,
        fontSize = 11.5.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
private fun ChangePasswordDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser = auth.currentUser
    val userEmail = currentUser?.email ?: UserSessionManager.getUserEmail(context)
    val hasPasswordProvider = remember(currentUser) {
        currentUser?.providerData?.any { it.providerId == "password" } == true
    }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrent by remember { mutableStateOf(false) }
    var showNew by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val sendResetEmail: () -> Unit = {
        if (userEmail.isBlank() || !userEmail.contains("@")) {
            errorMessage = "Email akun tidak ditemukan."
        } else {
            isLoading = true
            errorMessage = null
            successMessage = null
            auth.sendPasswordResetEmail(userEmail)
                .addOnSuccessListener {
                    isLoading = false
                    successMessage = "Link reset kata sandi telah dikirim ke $userEmail. Silakan cek kotak masuk atau folder spam email kamu."
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    errorMessage = e.localizedMessage ?: "Gagal mengirim email reset password."
                }
        }
    }

    val handleUpdatePassword: () -> Unit = {
        errorMessage = null
        successMessage = null
        when {
            currentUser == null || userEmail.isBlank() -> {
                errorMessage = "Sesi login Firebase tidak ditemukan. Silakan login ulang terlebih dahulu."
            }
            currentPassword.isBlank() -> {
                errorMessage = "Masukkan kata sandi saat ini terlebih dahulu."
            }
            newPassword.length < 6 -> {
                errorMessage = "Kata sandi baru minimal terdiri dari 6 karakter."
            }
            newPassword == currentPassword -> {
                errorMessage = "Kata sandi baru harus berbeda dari kata sandi lama."
            }
            newPassword != confirmPassword -> {
                errorMessage = "Konfirmasi kata sandi baru tidak cocok."
            }
            else -> {
                isLoading = true
                val credential = EmailAuthProvider.getCredential(userEmail, currentPassword)
                currentUser.reauthenticate(credential)
                    .addOnSuccessListener {
                        currentUser.updatePassword(newPassword)
                            .addOnSuccessListener {
                                isLoading = false
                                Toast.makeText(context, "Kata sandi berhasil diperbarui! 🔒", Toast.LENGTH_LONG).show()
                                onDismiss()
                            }
                            .addOnFailureListener { updateErr ->
                                isLoading = false
                                errorMessage = updateErr.localizedMessage ?: "Gagal memperbarui kata sandi."
                            }
                    }
                    .addOnFailureListener { authErr ->
                        isLoading = false
                        errorMessage = when {
                            authErr.message?.contains("password is invalid", ignoreCase = true) == true ||
                                authErr.message?.contains("invalid", ignoreCase = true) == true ||
                                authErr.message?.contains("credential", ignoreCase = true) == true ->
                                "Kata sandi saat ini salah. Periksa kembali atau gunakan fitur kirim email reset di bawah."
                            authErr.message?.contains("network", ignoreCase = true) == true ->
                                "Koneksi internet bermasalah. Periksa jaringan kamu."
                            else -> authErr.localizedMessage ?: "Verifikasi gagal. Coba lagi."
                        }
                    }
            }
        }
    }

    Dialog(onDismissRequest = { if (!isLoading) onDismiss() }) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = BorderStroke(1.dp, BorderSubtle),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 440.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(SageSoft, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = SagePrimary,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Ubah Kata Sandi",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        if (userEmail.isNotBlank()) {
                            Text(
                                text = userEmail,
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (currentUser != null && !hasPasswordProvider) {
                    Surface(
                        color = SageSoft,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Akun kamu saat ini masuk menggunakan Google Sign-In. Untuk mengatur kata sandi login manual via email, kirim tautan pengaturan kata sandi ke emailmu.",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            modifier = Modifier.padding(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = sendResetEmail,
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Kirim Link Atur Password ke Email", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    PasswordInputField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = "Kata Sandi Saat Ini",
                        visible = showCurrent,
                        onToggleVisible = { showCurrent = !showCurrent },
                        imeAction = ImeAction.Next
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PasswordInputField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = "Kata Sandi Baru (min. 6 karakter)",
                        visible = showNew,
                        onToggleVisible = { showNew = !showNew },
                        imeAction = ImeAction.Next
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PasswordInputField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Konfirmasi Kata Sandi Baru",
                        visible = showConfirm,
                        onToggleVisible = { showConfirm = !showConfirm },
                        imeAction = ImeAction.Done
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Lupa kata sandi saat ini? Kirim link reset ke email",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SagePrimary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(enabled = !isLoading) { sendResetEmail() }
                            .padding(vertical = 4.dp)
                    )
                }

                if (!errorMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = StatusDanger.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, StatusDanger.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = StatusDanger,
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                if (!successMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = StatusSuccess.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, StatusSuccess.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = successMessage!!,
                            color = StatusSuccess,
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss,
                        enabled = !isLoading
                    ) {
                        Text("Tutup", color = TextSecondary, fontWeight = FontWeight.SemiBold)
                    }

                    if (currentUser == null || hasPasswordProvider) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = handleUpdatePassword,
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = SagePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Simpan Sandi", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onToggleVisible: () -> Unit,
    imeAction: ImeAction
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 12.5.sp) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = SagePrimary,
                modifier = Modifier.size(19.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = onToggleVisible) {
                Icon(
                    imageVector = if (visible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(19.dp)
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SagePrimary,
            unfocusedBorderColor = BorderSubtle,
            focusedLabelColor = SagePrimary,
            unfocusedLabelColor = TextSecondary,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedContainerColor = SurfaceOffWhite,
            unfocusedContainerColor = SurfaceOffWhite
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SettingItemAction(
    title: String,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    iconTint: Color = SagePrimary,
    iconRight: ImageVector = Icons.Default.ChevronRight,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            if (iconTint == StatusDanger) StatusDanger.copy(alpha = 0.12f) else SageSoft,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(19.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (iconTint == StatusDanger) StatusDanger else TextPrimary
                )
                if (!subtitle.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = iconRight,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SettingItemToggle(
    title: String,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(SageSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = SagePrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                if (!subtitle.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = SagePrimary
            )
        )
    }
}
