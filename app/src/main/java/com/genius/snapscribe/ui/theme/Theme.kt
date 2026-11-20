package com.genius.snapscribe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.genius.snapscribe.domain.model.TextScale
import com.genius.snapscribe.domain.model.ThemeMode

@Composable
fun SnapScribeTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    highContrast: Boolean = false,
    textScale: TextScale = TextScale.STANDARD,
    content: @Composable () -> Unit,
) {
    val useDark = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        useDark && highContrast -> EspressoDarkHighContrastColorScheme
        useDark -> EspressoDarkColorScheme
        highContrast -> WarmLightHighContrastColorScheme
        else -> WarmLightColorScheme
    }

    val extendedColors = when {
        useDark && highContrast -> DarkHighContrastExtendedColors
        useDark -> DarkExtendedColors
        highContrast -> LightHighContrastExtendedColors
        else -> LightExtendedColors
    }

    val typography = if (useDark) EspressoTypography else WarmTypography
    val baseDensity = LocalDensity.current

    CompositionLocalProvider(
        LocalDensity provides Density(
            density = baseDensity.density,
            fontScale = baseDensity.fontScale * textScale.factor,
        ),
        LocalExtendedColors provides extendedColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = SnapShapes,
            content = content,
        )
    }
}

object SnapScribeTheme {
    val extendedColors: SnapExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColors.current
}
