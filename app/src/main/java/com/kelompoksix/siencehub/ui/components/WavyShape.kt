package com.kelompoksix.siencehub.ui.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class WavyShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            lineTo(0f, size.height * 0.8f) // Titik awal gelombang di kiri

            // Membuat kurva bezier untuk efek gelombang
            quadraticBezierTo(
                size.width * 0.25f, size.height * 0.7f,
                size.width * 0.5f, size.height * 0.85f
            )
            quadraticBezierTo(
                size.width * 0.75f, size.height * 1.0f,
                size.width, size.height * 0.75f
            )

            lineTo(size.width, 0f) // Tarik garis ke kanan atas
            close()
        }
        return Outline.Generic(path)
    }
}