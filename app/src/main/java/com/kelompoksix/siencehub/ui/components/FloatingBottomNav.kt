package com.kelompoksix.siencehub.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.ui.theme.BorderSubtle
import com.kelompoksix.siencehub.ui.theme.SagePrimary
import com.kelompoksix.siencehub.ui.theme.SageSoft
import com.kelompoksix.siencehub.ui.theme.SurfaceWhite
import com.kelompoksix.siencehub.ui.theme.TextSecondary

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val unselectedIcon: ImageVector = icon
)

@Composable
fun FloatingBottomNav(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem("Beranda", Icons.Filled.Home, Icons.Outlined.Home),
        NavItem("Materi", Icons.AutoMirrored.Filled.MenuBook, Icons.AutoMirrored.Outlined.MenuBook),
        NavItem("Profil", Icons.Filled.Person, Icons.Outlined.Person)
    )

    Surface(
        modifier = modifier
            .widthIn(min = 248.dp, max = 310.dp)
            .height(62.dp),
        shape = CircleShape,
        color = SurfaceWhite.copy(alpha = 0.97f),
        shadowElevation = 10.dp,
        tonalElevation = 2.dp,
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) SageSoft else Color.Transparent,
                    animationSpec = tween(220),
                    label = "navBg"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) SagePrimary else TextSecondary,
                    animationSpec = tween(220),
                    label = "navTint"
                )

                Row(
                    modifier = Modifier
                        .height(46.dp)
                        .clip(CircleShape)
                        .background(bgColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onItemSelected(index) }
                        .padding(horizontal = if (isSelected) 16.dp else 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isSelected) item.icon else item.unselectedIcon,
                        contentDescription = item.title,
                        tint = contentColor,
                        modifier = Modifier.size(23.dp)
                    )

                    AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn(tween(180)) + expandHorizontally(tween(220)),
                        exit = fadeOut(tween(140)) + shrinkHorizontally(tween(180))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                text = item.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SagePrimary,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
