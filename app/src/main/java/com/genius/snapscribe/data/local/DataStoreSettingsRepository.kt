package com.genius.snapscribe.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.genius.snapscribe.domain.model.AppSettings
import com.genius.snapscribe.domain.model.SnapSource
import com.genius.snapscribe.domain.model.TextScale
import com.genius.snapscribe.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.io.IOException

private val Context.snapScribeDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "snapscribe_settings",
)

class DataStoreSettingsRepository(
    context: Context,
) : SettingsRepository {

    private val dataStore = context.snapScribeDataStore

    override val settings: Flow<AppSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            val themeMode = prefs[KEY_THEME_MODE]
                ?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() }
                ?: ThemeMode.SYSTEM

            val highContrast = prefs[KEY_HIGH_CONTRAST] ?: false

            val textScale = prefs[KEY_TEXT_SCALE]
                ?.let { runCatching { TextScale.valueOf(it) }.getOrNull() }
                ?: TextScale.STANDARD

            val indexedFolders = prefs[KEY_INDEXED_FOLDERS]
                ?.mapNotNull { runCatching { SnapSource.valueOf(it) }.getOrNull() }
                ?.toSet()
                ?: setOf(SnapSource.SCREENSHOTS, SnapSource.WHATSAPP)

            val onboardingDone = prefs[KEY_ONBOARDING_DONE] ?: false
            val biometricLock = prefs[KEY_BIOMETRIC_LOCK] ?: false
            val cacheSizeMb = prefs[KEY_CACHE_SIZE_MB] ?: 142

            val recentSearches = prefs[KEY_RECENT_SEARCHES]?.let { raw ->
                runCatching { Json.decodeFromString<List<String>>(raw) }.getOrNull()
            } ?: AppSettings.DEFAULT_RECENT_SEARCHES

            AppSettings(
                themeMode = themeMode,
                highContrast = highContrast,
                textScale = textScale,
                indexedFolders = indexedFolders,
                onboardingDone = onboardingDone,
                biometricLockEnabled = biometricLock,
                cacheSizeMb = cacheSizeMb,
                recentSearches = recentSearches,
            )
        }

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { it[KEY_THEME_MODE] = mode.name }
    }

    override suspend fun setHighContrast(enabled: Boolean) {
        dataStore.edit { it[KEY_HIGH_CONTRAST] = enabled }
    }

    override suspend fun setTextScale(scale: TextScale) {
        dataStore.edit { it[KEY_TEXT_SCALE] = scale.name }
    }

    override suspend fun toggleIndexedFolder(source: SnapSource, enabled: Boolean) {
        dataStore.edit { prefs ->
            val current = prefs[KEY_INDEXED_FOLDERS]
                ?.mapNotNull { runCatching { SnapSource.valueOf(it) }.getOrNull() }
                ?.toMutableSet()
                ?: mutableSetOf(SnapSource.SCREENSHOTS, SnapSource.WHATSAPP)

            if (enabled) current.add(source) else current.remove(source)
            prefs[KEY_INDEXED_FOLDERS] = current.map { it.name }.toSet()
        }
    }

    override suspend fun setOnboardingDone(done: Boolean) {
        dataStore.edit { it[KEY_ONBOARDING_DONE] = done }
    }

    override suspend fun setBiometricLock(enabled: Boolean) {
        dataStore.edit { it[KEY_BIOMETRIC_LOCK] = enabled }
    }

    override suspend fun clearCache() {
        dataStore.edit { it[KEY_CACHE_SIZE_MB] = 0 }
    }

    override suspend fun addRecentSearch(query: String) {
        val clean = query.trim()
        if (clean.length < 2) return
        dataStore.edit { prefs ->
            val current = prefs[KEY_RECENT_SEARCHES]?.let { raw ->
                runCatching { Json.decodeFromString<List<String>>(raw) }.getOrNull()
            } ?: AppSettings.DEFAULT_RECENT_SEARCHES
            val updated = (listOf(clean) + current.filterNot { it.equals(clean, ignoreCase = true) })
                .take(8)
            prefs[KEY_RECENT_SEARCHES] = Json.encodeToString(updated)
        }
    }

    override suspend fun clearRecentSearches() {
        dataStore.edit { prefs ->
            prefs[KEY_RECENT_SEARCHES] = Json.encodeToString(emptyList<String>())
        }
    }

    private companion object {
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        val KEY_HIGH_CONTRAST = booleanPreferencesKey("high_contrast")
        val KEY_TEXT_SCALE = stringPreferencesKey("text_scale")
        val KEY_INDEXED_FOLDERS = stringSetPreferencesKey("indexed_folders")
        val KEY_ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val KEY_BIOMETRIC_LOCK = booleanPreferencesKey("biometric_lock")
        val KEY_CACHE_SIZE_MB = intPreferencesKey("cache_size_mb")
        val KEY_RECENT_SEARCHES = stringPreferencesKey("recent_searches")
    }
}
