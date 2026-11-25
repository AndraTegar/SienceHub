package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.CreamBeige
import com.kelompoksix.siencehub.ui.theme.SageDark
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextPrimary

@Composable
fun CardUtama(
    judulMateri: String,
    progress: Float,
    onClick: () -> Unit
) {
    val clampedProgress = progress.coerceIn(0f, 1f)

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CreamBeige),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, BorderSubtle.copy(alpha = 0.7f)),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 158.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = CircleShape,
                        color = SurfaceWhite.copy(alpha = 0.72f)
                    ) {
                        Text(
                            text = "LANJUTKAN BELAJAR",
                            color = SageDark,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = judulMateri,
                        color = TextPrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 25.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(SagePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Buka Materi",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Progres Belajar",
                        color = TextPrimary.copy(alpha = 0.65f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${(clampedProgress * 100).toInt()}%",
                        color = SageDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { clampedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = SagePrimary,
                    trackColor = SurfaceWhite.copy(alpha = 0.65f),
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF7A8B76)
@Composable
fun CardUtamaPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        CardUtama(
            judulMateri = "Fisika: Hukum Newton",
            progress = 0.75f,
            onClick = {}
        )
    }
}
