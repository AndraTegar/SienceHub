package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.data.models.KategoriMateri

@Composable
fun KartuKategori(
    item: KategoriMateri,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = item.warnaBg),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = item.tinggiCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = item.tinggiCard)
                .padding(18.dp)
        ) {
            // Lingkaran dekoratif lembut agar ilustrasi lebih menonjol & clean
            Box(
                modifier = Modifier
                    .size(item.gambarSize + 24.dp)
                    .align(Alignment.Center)
                    .offset(y = (-8).dp)
                    .background(Color.White.copy(alpha = 0.18f), CircleShape)
            )

            if (item.resId != null) {
                Image(
                    painter = painterResource(id = item.resId),
                    contentDescription = item.judul,
                    modifier = Modifier
                        .size(item.gambarSize)
                        .align(Alignment.Center)
                        .offset(y = (-8).dp),
                    contentScale = ContentScale.Fit
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.judul,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = item.warnaTeks,
                    letterSpacing = 0.8.sp
                )

                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = item.warnaTeks,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
