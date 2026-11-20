package com.genius.snapscribe.data.local

import com.genius.snapscribe.domain.model.AppSettings
import com.genius.snapscribe.domain.model.SnapSource
import com.genius.snapscribe.domain.model.TextScale
import com.genius.snapscribe.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setHighContrast(enabled: Boolean)
    suspend fun setTextScale(scale: TextScale)
    suspend fun toggleIndexedFolder(source: SnapSource, enabled: Boolean)
    suspend fun setOnboardingDone(done: Boolean)
    suspend fun setBiometricLock(enabled: Boolean)
    suspend fun clearCache()
    suspend fun addRecentSearch(query: String)
    suspend fun clearRecentSearches()
}
