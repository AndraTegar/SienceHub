package com.kelompoksix.siencehub.ui.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class WavyShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            lineTo(0f, size.height * 0.75f)
            cubicTo(
                size.width * 0.35f, size.height * 0.95f,
                size.width * 0.65f, size.height * 0.75f,
                size.width, size.height * 0.9f
            )
            lineTo(size.width, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}
