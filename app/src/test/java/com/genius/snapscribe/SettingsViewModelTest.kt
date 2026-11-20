package com.genius.snapscribe

import app.cash.turbine.test
import com.genius.snapscribe.data.repository.FakeSessionRepository
import com.genius.snapscribe.domain.model.SnapSource
import com.genius.snapscribe.domain.model.TextScale
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.ui.screens.settings.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun settingsChanges_updateUiStateAndRepository() = runTest {
        val settingsRepo = InMemorySettingsRepository()
        val sessionRepo = FakeSessionRepository(delayMs = 0L)
        val vm = SettingsViewModel(settingsRepo, sessionRepo)

        vm.uiState.test {
            val initial = awaitItem()
            assertEquals(ThemeMode.SYSTEM, initial.settings.themeMode)
            assertEquals(TextScale.STANDARD, initial.settings.textScale)
            assertEquals(142, initial.settings.cacheSizeMb)

            vm.setThemeMode(ThemeMode.DARK)
            assertEquals(ThemeMode.DARK, awaitItem().settings.themeMode)

            vm.setTextScale(TextScale.EXTRA_LARGE)
            assertEquals(TextScale.EXTRA_LARGE, awaitItem().settings.textScale)

            vm.setHighContrast(true)
            assertTrue(awaitItem().settings.highContrast)

            vm.toggleIndexedFolder(SnapSource.CAMERA, true)
            assertTrue(SnapSource.CAMERA in awaitItem().settings.indexedFolders)

            vm.toggleIndexedFolder(SnapSource.SCREENSHOTS, false)
            assertFalse(SnapSource.SCREENSHOTS in awaitItem().settings.indexedFolders)

            vm.clearCache()
            assertEquals(0, awaitItem().settings.cacheSizeMb)
        }
    }
}
