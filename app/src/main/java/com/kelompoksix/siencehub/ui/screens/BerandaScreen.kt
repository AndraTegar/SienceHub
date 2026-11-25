package com.kelompoksix.siencehub.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kelompoksix.siencehub.data.models.TopikMateri
import com.kelompoksix.siencehub.data.repositories.LocalLearningRepository
import com.kelompoksix.siencehub.data.repositories.MateriRepository
import com.kelompoksix.siencehub.data.repositories.NotifikasiRepository
import com.kelompoksix.siencehub.data.repositories.UserProgressRepository
import com.kelompoksix.siencehub.ui.components.CardAksi
import com.kelompoksix.siencehub.ui.components.CardUtama
import com.kelompoksix.siencehub.ui.components.SearchBarMateri
import com.kelompoksix.siencehub.ui.theme.ResponsiveContentContainer
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SageSoft
import com.kelompoksix.siencehub.ui.theme.SurfaceOffWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary
import com.kelompoksix.siencehub.ui.theme.TextSecondary
import com.kelompoksix.siencehub.ui.theme.rememberResponsiveMetrics
import com.kelompoksix.siencehub.ui.viewmodels.BerandaViewModel
import com.kelompoksix.siencehub.utils.AppSettingsManager
import com.kelompoksix.siencehub.utils.UserSessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(
    viewModel: BerandaViewModel = viewModel(),
    onNavigateToMateri: (String) -> Unit = {},
    onHasilCari: (TopikMateri, Int?) -> Unit = { _, _ -> },
    onNavigateToEksperimen: () -> Unit = {},
    onMulaiKuis: (String) -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()
    val metrics = rememberResponsiveMetrics()

    val context = LocalContext.current
    val userName = UserSessionManager.getUserName(context).ifBlank { "Siswa" }

    val notifikasiList by NotifikasiRepository.notifikasiList.collectAsState()
    val unreadCount = remember(notifikasiList) { notifikasiList.count { !it.isDibaca } }

    val faktaSains = viewModel.faktaSains
    val isLoadingFakta = viewModel.isLoadingFakta

    LaunchedEffect(context) {
        UserProgressRepository.loadProgress(context)
        LocalLearningRepository.refreshStats(context)
    }

    val progressMap by UserProgressRepository.subjectProgressFlow.collectAsState()
    val learningStats by LocalLearningRepository.learningStatsFlow.collectAsState()

    val daftarMateri = remember(learningStats, progressMap) {
        val semuaTopik = MateriRepository.getDaftarTopik()
        val urutanTerakhir = learningStats.recentlyOpenedTopicIds
        semuaTopik.sortedWith(
            compareBy { topik ->
                val idx = urutanTerakhir.indexOf(topik.id)
                if (idx >= 0) idx else Int.MAX_VALUE
            }
        ).map { topik ->
            Pair(topik, learningStats.progressFor(topik))
        }
    }

    val pagerState = rememberPagerState(pageCount = { daftarMateri.size })

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = SurfaceOffWhite,
        topBar = {
            TopAppBar(
                title = {
                    ResponsiveContentContainer(maxWidth = 760.dp) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SearchBarMateri(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                onHasilClick = onHasilCari
                            )

                            Box(contentAlignment = Alignment.TopEnd) {
                                IconButton(
                                    onClick = onNotificationClick,
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(Color.White.copy(alpha = 0.20f), CircleShape)
                                        .border(1.dp, Color.White.copy(alpha = 0.28f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notifikasi",
                                        tint = Color.White,
                                        modifier = Modifier.size(21.dp)
                                    )
                                }

                                if (unreadCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .offset(x = (-2).dp, y = 2.dp)
                                            .size(11.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFF3B30))
                                            .border(1.5.dp, SagePrimary, CircleShape)
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SagePrimary,
                    scrolledContainerColor = SagePrimary
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SagePrimary)
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            ResponsiveContentContainer(maxWidth = 760.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = metrics.horizontalPadding)
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Halo, $userName! 👋",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Siap untuk belajar sains hari ini?",
                        fontSize = 13.5.sp,
                        color = Color.White.copy(alpha = 0.86f)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Kartu Status Level, XP & Streak Real-Time
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.16f)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.24f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp, horizontal = 18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(Color(0xFFE1F5FE), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFF0288D1),
                                            modifier = Modifier.size(21.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Level ${learningStats.level}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "${learningStats.levelTitle} • ${learningStats.experiencePoints} XP",
                                            fontSize = 11.5.sp,
                                            color = Color.White.copy(alpha = 0.85f)
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .height(32.dp)
                                        .width(1.dp)
                                        .background(Color.White.copy(alpha = 0.28f))
                                )

                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(Color(0xFFFFF3E0), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.LocalFireDepartment,
                                            contentDescription = null,
                                            tint = Color(0xFFFF6D00),
                                            modifier = Modifier.size(21.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "${learningStats.streakDays} Hari",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = if (learningStats.streakDays > 0) "Streak Aktif" else "Mulai Streak!",
                                            fontSize = 11.5.sp,
                                            color = Color.White.copy(alpha = 0.85f)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Menuju Level ${learningStats.level + 1}",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.82f)
                                )
                                Text(
                                    text = "${learningStats.xpInCurrentLevel}/${learningStats.xpForNextLevel} XP",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            LinearProgressIndicator(
                                progress = {
                                    learningStats.xpInCurrentLevel.toFloat() / learningStats.xpForNextLevel.toFloat()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                                    .clip(CircleShape),
                                color = Color(0xFFFDE047),
                                trackColor = Color.White.copy(alpha = 0.24f),
                                strokeCap = StrokeCap.Round
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lanjutkan Belajar",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${pagerState.currentPage + 1}/${daftarMateri.size}",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth(),
                        pageSpacing = 14.dp
                    ) { page ->
                        val (topik, prog) = daftarMateri[page]
                        CardUtama(
                            judulMateri = topik.judul,
                            progress = prog,
                            onClick = { onNavigateToMateri(topik.judul) }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp, bottom = 24.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(daftarMateri.size) { iteration ->
                            val isSelected = pagerState.currentPage == iteration
                            val dotWidth by animateDpAsState(
                                targetValue = if (isSelected) 24.dp else 8.dp,
                                label = "pagerDot"
                            )
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.5.dp)
                                    .height(7.dp)
                                    .width(dotWidth)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) Color.White else Color.White.copy(alpha = 0.4f)
                                    )
                            )
                        }
                    }
                }
            }

            // Bagian Bawah: Sheet Melengkung Adaptif (Light/Dark)
            Surface(
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = SurfaceOffWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 360.dp)
            ) {
                ResponsiveContentContainer(maxWidth = 760.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(
                                start = metrics.horizontalPadding,
                                end = metrics.horizontalPadding,
                                top = 26.dp,
                                bottom = 104.dp
                            )
                    ) {
                        Text(
                            text = "Aktivitas Seru Hari Ini",
                            fontSize = 18.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Eksplorasi simulasi interaktif & wawasan baru",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(18.dp))

                        CardAksi(
                            judul = "Eksperimen Virtual \uD83D\uDD2C",
                            deskripsi = "Uji laju fotosintesis dengan Sensor Cahaya, Hukum Newton (Accelerometer), gravitasi planet, & pH.",
                            teksTombol = "Mainkan",
                            ikon = Icons.Default.Science,
                            warnaBackground = SageSoft,
                            warnaTombol = SagePrimary,
                            onClick = onNavigateToEksperimen
                        )

                        CardAksi(
                            judul = "Fakta Menarik \uD83D\uDCA1",
                            deskripsi = if (isLoadingFakta) "Memuat fakta menarik hari ini..." else faktaSains,
                            teksTombol = if (isLoadingFakta) "Memuat..." else "Fakta Baru",
                            ikon = Icons.Default.Lightbulb,
                            warnaBackground = if (AppSettingsManager.isDarkMode) Color(0xFF2B2416) else Color(0xFFFFFBEB),
                            warnaTombol = Color(0xFFD97706),
                            onClick = { viewModel.muatFaktaBaru() }
                        )
                    }
                }
            }
        }
    }
}
