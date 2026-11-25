package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.Soal
import com.kelompoksix.siencehub.data.repositories.MateriRepository
import com.kelompoksix.siencehub.data.repositories.QuizRepository
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

private data class ReviewEntry(
    val number: Int,
    val soal: Soal,
    val userChoice: Int?
) {
    val isCorrect: Boolean
        get() = userChoice != null && userChoice == soal.jawabanBenar

    val isUnanswered: Boolean
        get() = userChoice == null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizReviewScreen(
    subjectName: String,
    onBackClick: () -> Unit
) {
    val displaySubject = remember(subjectName) { MateriRepository.normalizeSubjectName(subjectName) }
    val session = remember(subjectName) { QuizRepository.getSessionResult(subjectName) }
    val daftarSoal = remember(subjectName, session) {
        session?.questions ?: QuizRepository.getSoalBySubject(subjectName)
    }
    val userAnswers = remember(session) { session?.userAnswers.orEmpty() }
    val hasSessionAnswers = session != null

    val allEntries = remember(daftarSoal, userAnswers) {
        daftarSoal.mapIndexed { idx, soal ->
            ReviewEntry(
                number = idx + 1,
                soal = soal,
                userChoice = userAnswers[idx]
            )
        }
    }

    var selectedFilter by remember { mutableIntStateOf(0) } // 0 = Semua, 1 = Salah/Kosong, 2 = Benar

    val wrongOrEmptyCount = remember(allEntries) { allEntries.count { !it.isCorrect } }
    val correctCount = remember(allEntries) { allEntries.count { it.isCorrect } }

    val filteredEntries = remember(allEntries, selectedFilter, hasSessionAnswers) {
        if (!hasSessionAnswers) {
            allEntries
        } else {
            when (selectedFilter) {
                1 -> allEntries.filter { !it.isCorrect }
                2 -> allEntries.filter { it.isCorrect }
                else -> allEntries
            }
        }
    }

    val metrics = rememberResponsiveMetrics()

    Scaffold(
        containerColor = SurfaceOffWhite,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Pembahasan Kuis",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (session != null) {
                                "$displaySubject • Skor ${session.score} pt ($correctCount/${daftarSoal.size} Benar)"
                            } else {
                                displaySubject
                            },
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.88f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SagePrimary
                )
            )
        }
    ) { padding ->
        ResponsiveContentContainer(
            maxWidth = 700.dp,
            modifier = Modifier.padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(
                    horizontal = metrics.horizontalPadding,
                    vertical = 18.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (hasSessionAnswers) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ReviewFilterChip(
                                label = "Semua (${allEntries.size})",
                                selected = selectedFilter == 0,
                                onClick = { selectedFilter = 0 }
                            )
                            ReviewFilterChip(
                                label = "Perlu Evaluasi ($wrongOrEmptyCount)",
                                selected = selectedFilter == 1,
                                onClick = { selectedFilter = 1 }
                            )
                            ReviewFilterChip(
                                label = "Benar ($correctCount)",
                                selected = selectedFilter == 2,
                                onClick = { selectedFilter = 2 }
                            )
                        }
                    }
                }

                if (filteredEntries.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                            border = BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (selectedFilter == 1) {
                                    "Luar biasa! Tidak ada jawaban yang salah atau kosong pada kuis ini. 🎉"
                                } else {
                                    "Belum ada soal pada kategori filter ini."
                                },
                                color = TextSecondary,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                }

                items(
                    items = filteredEntries,
                    key = { it.number }
                ) { entry ->
                    ReviewQuestionCard(
                        entry = entry,
                        totalQuestions = daftarSoal.size,
                        showUserComparison = hasSessionAnswers
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        },
        shape = CircleShape,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = SagePrimary,
            selectedLabelColor = Color.White,
            containerColor = SurfaceWhite,
            labelColor = TextSecondary
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = BorderSubtle,
            selectedBorderColor = SagePrimary
        )
    )
}

@Composable
private fun ReviewQuestionCard(
    entry: ReviewEntry,
    totalQuestions: Int,
    showUserComparison: Boolean
) {
    val soal = entry.soal
    val statusBorderColor = when {
        !showUserComparison -> BorderSubtle
        entry.isCorrect -> StatusSuccess.copy(alpha = 0.45f)
        entry.isUnanswered -> Color(0xFFD97706).copy(alpha = 0.45f)
        else -> StatusDanger.copy(alpha = 0.45f)
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.2.dp, statusBorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SageSoft,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${entry.number}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = SagePrimary
                            )
                        }
                    }
                    Text(
                        text = "Soal ${entry.number} dari $totalQuestions",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }

                if (showUserComparison) {
                    val (badgeText, badgeBg, badgeTextCol) = when {
                        entry.isCorrect -> Triple("Benar +20 pt", StatusSuccess.copy(alpha = 0.16f), StatusSuccess)
                        entry.isUnanswered -> Triple("Tidak Dijawab", Color(0xFFD97706).copy(alpha = 0.16f), Color(0xFFD97706))
                        else -> Triple("Jawaban Salah", StatusDanger.copy(alpha = 0.16f), StatusDanger)
                    }
                    Surface(
                        shape = CircleShape,
                        color = badgeBg
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when {
                                    entry.isCorrect -> Icons.Default.CheckCircle
                                    entry.isUnanswered -> Icons.AutoMirrored.Filled.HelpOutline
                                    else -> Icons.Default.Cancel
                                },
                                contentDescription = null,
                                tint = badgeTextCol,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = badgeText,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = badgeTextCol
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = soal.pertanyaan,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Daftar Ke-4 Opsi dengan Indikator Jawaban User vs Kunci Jawaban
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                soal.opsi.forEachIndexed { optIdx, optText ->
                    val letter = ('A' + optIdx).toString()
                    val isCorrectOption = optIdx == soal.jawabanBenar
                    val isUserPicked = showUserComparison && entry.userChoice == optIdx

                    val bgColor = when {
                        isCorrectOption -> StatusSuccess.copy(alpha = 0.15f)
                        isUserPicked -> StatusDanger.copy(alpha = 0.15f)
                        else -> SurfaceOffWhite
                    }
                    val borderColor = when {
                        isCorrectOption -> StatusSuccess
                        isUserPicked -> StatusDanger
                        else -> BorderSubtle
                    }
                    val labelTag = when {
                        isCorrectOption && isUserPicked -> "Jawabanmu Benar"
                        isCorrectOption -> "Kunci Jawaban"
                        isUserPicked -> "Jawabanmu"
                        else -> null
                    }
                    val accentColor = when {
                        isCorrectOption -> StatusSuccess
                        isUserPicked -> StatusDanger
                        else -> TextSecondary
                    }

                    Surface(
                        color = bgColor,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(if (isCorrectOption || isUserPicked) 1.5.dp else 1.dp, borderColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .background(
                                        when {
                                            isCorrectOption -> StatusSuccess
                                            isUserPicked -> StatusDanger
                                            else -> BorderSubtle
                                        },
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = letter,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCorrectOption || isUserPicked) Color.White else TextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = optText,
                                fontSize = 13.5.sp,
                                fontWeight = if (isCorrectOption || isUserPicked) FontWeight.Bold else FontWeight.Normal,
                                color = TextPrimary,
                                lineHeight = 19.sp,
                                modifier = Modifier.weight(1f)
                            )

                            if (labelTag != null) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = labelTag,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = accentColor
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Kotak Pembahasan
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceOffWhite, RoundedCornerShape(12.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier
                        .size(20.dp)
                        .padding(top = 2.dp)
                )
                Column {
                    Text(
                        text = "Pembahasan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = soal.pembahasan,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}
