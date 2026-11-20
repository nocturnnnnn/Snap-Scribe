package com.genius.snapscribe

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.ui.SnapScribeApp
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val container = (application as SnapScribeApplication).container
        val initialSettingsFlow = container.settingsRepository.settings.stateIn(
            scope = lifecycleScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

        splashScreen.setKeepOnScreenCondition {
            initialSettingsFlow.value == null
        }

        enableEdgeToEdge()

        setContent {
            val settingsOrNull by initialSettingsFlow.collectAsStateWithLifecycle()
            val settings = settingsOrNull ?: return@setContent
            val initialOnboardingDone = remember { settings.onboardingDone }
            val systemDark = isSystemInDarkTheme()
            val useDark = when (settings.themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> systemDark
            }

            DisposableEffect(useDark) {
                enableEdgeToEdge(
                    statusBarStyle = if (useDark) {
                        SystemBarStyle.dark(Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                    },
                    navigationBarStyle = if (useDark) {
                        SystemBarStyle.dark(Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                    },
                )
                onDispose {}
            }

            SnapScribeTheme(
                themeMode = settings.themeMode,
                highContrast = settings.highContrast,
                textScale = settings.textScale,
            ) {
                SnapScribeApp(
                    container = container,
                    initialOnboardingDone = initialOnboardingDone,
                    currentOnboardingDone = settings.onboardingDone,
                )
            }
        }
    }
}
