package com.kelompoksix.siencehub.ui.theme

import androidx.compose.ui.graphics.Color
import com.kelompoksix.siencehub.utils.AppSettingsManager

// Palet Utama ScienceHub
val GreenBackground = Color(0xFF778873)
val GreenLight = Color(0xFFA1BC98)
val Cream90 = Color(0xFFDCCFC0)
val CreamLight = Color(0xFFFDF6ED)
val GrayColor = Color(0xFF424242)

// Token Warna Semantik Adaptif (Light & Dark Mode Real-Time)
val SagePrimary: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFF5D7259) else Color(0xFF7A8B76)

val SageDark: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFFA7C4A0) else Color(0xFF5C6D58)

val SageSoft: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFF233025) else Color(0xFFEBF0EA)

val SageContainer: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFF29382B) else Color(0xFFE3EAE1)

val CreamBeige: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFF263228) else Color(0xFFE5D9C7)

val SurfaceWhite: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFF1D2420) else Color(0xFFFFFFFF)

val SurfaceOffWhite: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFF121714) else Color(0xFFF7F9F8)

val SurfaceMuted: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFF252E28) else Color(0xFFF1F5F9)

val TextPrimary: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFFF1F5F9) else Color(0xFF1E293B)

val TextSecondary: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

val BorderSubtle: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFF2E3A32) else Color(0xFFE2E8F0)

val AccentPurple: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFFA855F7) else Color(0xFF8B4CFC)

val AccentAmber = Color(0xFFFFC94D)

val AccentBlue: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFF60A5FA) else Color(0xFF1E3A8A)

val AccentBlueSoft: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFF1E2C3E) else Color(0xFFDCE8F5)

val StatusSuccess: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFF22C55E) else Color(0xFF16A34A)

val StatusDanger: Color
    get() = if (AppSettingsManager.isDarkMode) Color(0xFFEF4444) else Color(0xFFDC2626)
