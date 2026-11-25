package com.kelompoksix.siencehub.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ResponsiveMetrics(
    val screenWidthDp: Int,
    val screenHeightDp: Int,
    val isCompactWidth: Boolean,
    val isTablet: Boolean,
    val isShortScreen: Boolean,
    val horizontalPadding: Dp,
    val contentMaxWidth: Dp,
    val formMaxWidth: Dp
)

@Composable
fun rememberResponsiveMetrics(): ResponsiveMetrics {
    val config = LocalConfiguration.current
    val w = config.screenWidthDp
    val h = config.screenHeightDp
    return remember(w, h) {
        val isSmallPhone = w < 360
        val isTablet = w >= 600
        ResponsiveMetrics(
            screenWidthDp = w,
            screenHeightDp = h,
            isCompactWidth = w < 600,
            isTablet = isTablet,
            isShortScreen = h < 680,
            horizontalPadding = when {
                isSmallPhone -> 16.dp
                isTablet -> 32.dp
                else -> 20.dp
            },
            contentMaxWidth = if (isTablet) 760.dp else Dp.Unspecified,
            formMaxWidth = 480.dp
        )
    }
}

@Composable
fun ResponsiveContentContainer(
    modifier: Modifier = Modifier,
    maxWidth: Dp = 720.dp,
    contentAlignment: Alignment = Alignment.TopCenter,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = contentAlignment
    ) {
        Box(modifier = Modifier.widthIn(max = maxWidth).fillMaxWidth()) {
            content()
        }
    }
}
