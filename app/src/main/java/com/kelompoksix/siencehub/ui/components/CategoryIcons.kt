package com.kelompoksix.siencehub.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AtomIcon(
    modifier: Modifier = Modifier.size(48.dp)
) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radiusX = size.width * 0.42f
        val radiusY = size.height * 0.16f

        // Draw 3 orbital ellipses at angles 0, 60, 120 degrees
        val orbitColors = listOf(Color(0xFFE91E63), Color(0xFF00BCD4), Color(0xFFFFEB3B))

        for (i in 0 until 3) {
            val angleRad = Math.toRadians(i * 60.0)
            val path = Path()
            val steps = 60
            for (j in 0..steps) {
                val t = Math.toRadians((j * (360.0 / steps)))
                val unrotatedX = radiusX * cos(t)
                val unrotatedY = radiusY * sin(t)

                val rotatedX = center.x + (unrotatedX * cos(angleRad) - unrotatedY * sin(angleRad)).toFloat()
                val rotatedY = center.y + (unrotatedX * sin(angleRad) + unrotatedY * cos(angleRad)).toFloat()

                if (j == 0) path.moveTo(rotatedX, rotatedY) else path.lineTo(rotatedX, rotatedY)
            }
            drawPath(
                path = path,
                color = orbitColors[i].copy(alpha = 0.8f),
                style = Stroke(width = 3.5f)
            )
        }

        // Nucleus (protons & neutrons)
        drawCircle(color = Color(0xFFFF5722), radius = 6f, center = Offset(center.x - 3f, center.y - 3f))
        drawCircle(color = Color(0xFF2196F3), radius = 6f, center = Offset(center.x + 3f, center.y - 2f))
        drawCircle(color = Color(0xFFFFEB3B), radius = 5.5f, center = Offset(center.x, center.y + 4f))

        // Electron dots on orbits
        drawCircle(color = Color(0xFFFF4081), radius = 4f, center = Offset(center.x + radiusX * 0.8f, center.y - 10f))
        drawCircle(color = Color(0xFF00E5FF), radius = 4f, center = Offset(center.x - radiusX * 0.6f, center.y + 12f))
    }
}

@Composable
fun FlaskIcon(
    modifier: Modifier = Modifier.size(48.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Flask outline
        val flaskPath = Path().apply {
            moveTo(w * 0.4f, h * 0.15f)
            lineTo(w * 0.6f, h * 0.15f)
            lineTo(w * 0.6f, h * 0.40f)
            lineTo(w * 0.85f, h * 0.82f)
            quadraticTo(w * 0.88f, h * 0.88f, w * 0.78f, h * 0.88f)
            lineTo(w * 0.22f, h * 0.88f)
            quadraticTo(w * 0.12f, h * 0.88f, w * 0.15f, h * 0.82f)
            lineTo(w * 0.40f, h * 0.40f)
            close()
        }

        // Pink liquid inside
        val liquidPath = Path().apply {
            moveTo(w * 0.32f, h * 0.55f)
            quadraticTo(w * 0.50f, h * 0.50f, w * 0.68f, h * 0.55f)
            lineTo(w * 0.82f, h * 0.82f)
            quadraticTo(w * 0.85f, h * 0.86f, w * 0.78f, h * 0.86f)
            lineTo(w * 0.22f, h * 0.86f)
            quadraticTo(w * 0.15f, h * 0.86f, w * 0.18f, h * 0.82f)
            close()
        }

        drawPath(path = liquidPath, color = Color(0xFFE91E63).copy(alpha = 0.75f))
        drawPath(path = flaskPath, color = Color(0xFF2C5E55), style = Stroke(width = 3.5f))

        // Bubbles rising
        drawCircle(color = Color(0xFFE91E63), radius = 3f, center = Offset(w * 0.48f, h * 0.35f))
        drawCircle(color = Color(0xFFE91E63), radius = 4f, center = Offset(w * 0.55f, h * 0.25f))
    }
}

@Composable
fun DnaIcon(
    modifier: Modifier = Modifier.size(48.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val steps = 8
        val magenta = Color(0xFFD81B60)
        val purple = Color(0xFF8E24AA)

        for (i in 0..steps) {
            val y = h * 0.12f + (h * 0.76f / steps) * i
            val phase = Math.toRadians((i * (360.0 / steps) * 0.8))
            val x1 = (w * 0.5f + (w * 0.30f) * sin(phase)).toFloat()
            val x2 = (w * 0.5f - (w * 0.30f) * sin(phase)).toFloat()

            // Horizontal connection bar
            drawLine(
                color = magenta.copy(alpha = 0.6f),
                start = Offset(x1, y),
                end = Offset(x2, y),
                strokeWidth = 3f
            )

            // Strand nodes
            drawCircle(color = magenta, radius = 5.5f, center = Offset(x1, y))
            drawCircle(color = purple, radius = 5.5f, center = Offset(x2, y))
        }
    }
}

@Composable
fun GalaxyIcon(
    modifier: Modifier = Modifier.size(48.dp)
) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radiusX = size.width * 0.42f
        val radiusY = size.height * 0.22f

        val angleRad = Math.toRadians(-30.0)

        // Spiral rings
        for (i in 1..3) {
            val rx = radiusX * (i / 3f)
            val ry = radiusY * (i / 3f)
            val path = Path()
            val steps = 40
            for (j in 0..steps) {
                val t = Math.toRadians((j * (360.0 / steps)))
                val ux = rx * cos(t)
                val uy = ry * sin(t)
                val rxPos = center.x + (ux * cos(angleRad) - uy * sin(angleRad)).toFloat()
                val ryPos = center.y + (ux * sin(angleRad) + uy * cos(angleRad)).toFloat()
                if (j == 0) path.moveTo(rxPos, ryPos) else path.lineTo(rxPos, ryPos)
            }
            drawPath(
                path = path,
                color = when (i) {
                    1 -> Color(0xFFFF1744)
                    2 -> Color(0xFFAA00FF)
                    else -> Color(0xFF2979FF)
                },
                style = Stroke(width = 4f)
            )
        }

        // Core bright center
        drawCircle(color = Color(0xFFFFEA00), radius = 9f, center = center)
        drawCircle(color = Color.White, radius = 5f, center = center)
    }
}

@Composable
fun EarthIcon(
    modifier: Modifier = Modifier.size(48.dp)
) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.width * 0.38f

        // Globe base
        drawCircle(color = Color(0xFF29B6F6), radius = radius, center = center)

        // Continents (green shapes)
        drawCircle(color = Color(0xFF66BB6A), radius = radius * 0.45f, center = Offset(center.x - radius * 0.2f, center.y - radius * 0.2f))
        drawCircle(color = Color(0xFF66BB6A), radius = radius * 0.4f, center = Offset(center.x + radius * 0.3f, center.y + radius * 0.2f))
        drawCircle(color = Color(0xFF0288D1), radius = radius, center = center, style = Stroke(width = 3f))
    }
}
