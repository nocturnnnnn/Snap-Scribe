package com.genius.snapscribe

import com.genius.snapscribe.data.local.SettingsRepository
import com.genius.snapscribe.domain.model.AppSettings
import com.genius.snapscribe.domain.model.SnapSource
import com.genius.snapscribe.domain.model.TextScale
import com.genius.snapscribe.domain.model.ThemeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

class InMemorySettingsRepository(
    initial: AppSettings = AppSettings(),
) : SettingsRepository {

    private val _settings = MutableStateFlow(initial)
    override val settings: Flow<AppSettings> = _settings.asStateFlow()

    override suspend fun setThemeMode(mode: ThemeMode) {
        _settings.update { it.copy(themeMode = mode) }
    }

    override suspend fun setHighContrast(enabled: Boolean) {
        _settings.update { it.copy(highContrast = enabled) }
    }

    override suspend fun setTextScale(scale: TextScale) {
        _settings.update { it.copy(textScale = scale) }
    }

    override suspend fun toggleIndexedFolder(source: SnapSource, enabled: Boolean) {
        _settings.update { current ->
            val updated = current.indexedFolders.toMutableSet()
            if (enabled) updated.add(source) else updated.remove(source)
            current.copy(indexedFolders = updated)
        }
    }

    override suspend fun setOnboardingDone(done: Boolean) {
        _settings.update { it.copy(onboardingDone = done) }
    }

    override suspend fun setBiometricLock(enabled: Boolean) {
        _settings.update { it.copy(biometricLockEnabled = enabled) }
    }

    override suspend fun clearCache() {
        _settings.update { it.copy(cacheSizeMb = 0) }
    }

    override suspend fun addRecentSearch(query: String) {
        val clean = query.trim()
        if (clean.length < 2) return
        _settings.update { current ->
            val updated = (listOf(clean) + current.recentSearches.filterNot {
                it.equals(clean, ignoreCase = true)
            }).take(8)
            current.copy(recentSearches = updated)
        }
    }

    override suspend fun clearRecentSearches() {
        _settings.update { it.copy(recentSearches = emptyList()) }
    }
}
