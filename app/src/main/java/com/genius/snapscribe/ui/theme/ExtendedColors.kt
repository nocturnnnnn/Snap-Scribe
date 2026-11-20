package com.genius.snapscribe.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.genius.snapscribe.domain.model.SnapCategory

@Immutable
data class SnapExtendedColors(
    val receiptsContainer: Color,
    val onReceiptsContainer: Color,
    val codesContainer: Color,
    val onCodesContainer: Color,
    val addressesContainer: Color,
    val onAddressesContainer: Color,
    val notesContainer: Color,
    val onNotesContainer: Color,
    val successContainer: Color,
    val onSuccess: Color,
    val ocrHighlightFill: Color,
    val ocrHighlightBorder: Color,
    val cardBorder: Color,
    val isDark: Boolean,
) {
    fun categoryColors(category: SnapCategory): Pair<Color, Color> = when (category) {
        SnapCategory.RECEIPTS -> receiptsContainer to onReceiptsContainer
        SnapCategory.CODES_LINKS -> codesContainer to onCodesContainer
        SnapCategory.ADDRESSES -> addressesContainer to onAddressesContainer
        SnapCategory.NOTES -> notesContainer to onNotesContainer
    }
}

val LightExtendedColors = SnapExtendedColors(
    receiptsContainer = Color(0xFFF5E6CA),
    onReceiptsContainer = Color(0xFF3B2809),
    codesContainer = Color(0xFFFBE8E4),
    onCodesContainer = Color(0xFF5D1B0E),
    addressesContainer = Color(0xFFE3ECE4),
    onAddressesContainer = Color(0xFF193B24),
    notesContainer = Color(0xFFE3EDF7),
    onNotesContainer = Color(0xFF13314D),
    successContainer = Color(0xFFE3ECE4),
    onSuccess = Color(0xFF2E5E3E),
    ocrHighlightFill = Color(0x55E89A3C),
    ocrHighlightBorder = Color(0xFFC54F2C),
    cardBorder = Color(0xFFE8E2D9),
    isDark = false,
)

val LightHighContrastExtendedColors = SnapExtendedColors(
    receiptsContainer = Color(0xFFEFD9B0),
    onReceiptsContainer = Color(0xFF211502),
    codesContainer = Color(0xFFF7D5CE),
    onCodesContainer = Color(0xFF3B0E06),
    addressesContainer = Color(0xFFCFE2D2),
    onAddressesContainer = Color(0xFF0A2413),
    notesContainer = Color(0xFFCFE0F2),
    onNotesContainer = Color(0xFF091D30),
    successContainer = Color(0xFFCFE2D2),
    onSuccess = Color(0xFF133B21),
    ocrHighlightFill = Color(0x66E89A3C),
    ocrHighlightBorder = Color(0xFF7D240A),
    cardBorder = Color(0xFF8A7970),
    isDark = false,
)

val DarkExtendedColors = SnapExtendedColors(
    receiptsContainer = Color(0xFF382A1A),
    onReceiptsContainer = Color(0xFFF5D8A8),
    codesContainer = Color(0xFF3A221E),
    onCodesContainer = Color(0xFFFFB8A6),
    addressesContainer = Color(0xFF1E3325),
    onAddressesContainer = Color(0xFFB6E6C3),
    notesContainer = Color(0xFF1C2C3B),
    onNotesContainer = Color(0xFFBAD8F7),
    successContainer = Color(0xFF1F3527),
    onSuccess = Color(0xFF9EE0B2),
    ocrHighlightFill = Color(0x4DE89A3C),
    ocrHighlightBorder = Color(0xFFFFB86A),
    cardBorder = Color(0xFF3D322B),
    isDark = true,
)

val DarkHighContrastExtendedColors = SnapExtendedColors(
    receiptsContainer = Color(0xFF473520),
    onReceiptsContainer = Color(0xFFFFE7BE),
    codesContainer = Color(0xFF4A2A24),
    onCodesContainer = Color(0xFFFFD0C4),
    addressesContainer = Color(0xFF25412E),
    onAddressesContainer = Color(0xFFD2F7DC),
    notesContainer = Color(0xFF22374B),
    onNotesContainer = Color(0xFFD6EBFF),
    successContainer = Color(0xFF254230),
    onSuccess = Color(0xFFC4F5D3),
    ocrHighlightFill = Color(0x66FFB86A),
    ocrHighlightBorder = Color(0xFFFFD599),
    cardBorder = Color(0xFF8C7768),
    isDark = true,
)

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }
