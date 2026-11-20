package com.genius.snapscribe.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genius.snapscribe.data.local.SettingsRepository
import com.genius.snapscribe.data.repository.SessionRepository
import com.genius.snapscribe.domain.model.AppSettings
import com.genius.snapscribe.domain.model.BillingCycle
import com.genius.snapscribe.domain.model.Session
import com.genius.snapscribe.domain.model.SnapSource
import com.genius.snapscribe.domain.model.TextScale
import com.genius.snapscribe.domain.model.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val isPro: Boolean = false,
    val proCycle: BillingCycle? = null,
    val userInitials: String? = null,
    val isSignedIn: Boolean = false,
)

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    sessionRepository: SessionRepository,
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.settings,
        sessionRepository.session,
    ) { settings, session ->
        val initials = when (session) {
            Session.Guest -> null
            is Session.FreeAccount -> session.user.initials
            is Session.Pro -> session.user.initials
        }
        SettingsUiState(
            settings = settings,
            isPro = session is Session.Pro,
            proCycle = (session as? Session.Pro)?.cycle,
            userInitials = initials,
            isSignedIn = session !is Session.Guest,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState(),
    )

    fun setTextScale(scale: TextScale) {
        viewModelScope.launch {
            settingsRepository.setTextScale(scale)
        }
    }

    fun setHighContrast(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setHighContrast(enabled)
        }
    }

    fun toggleIndexedFolder(source: SnapSource, enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.toggleIndexedFolder(source, enabled)
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(mode)
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            settingsRepository.clearCache()
        }
    }
}
