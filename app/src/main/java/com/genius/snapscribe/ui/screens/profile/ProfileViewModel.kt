package com.genius.snapscribe.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genius.snapscribe.data.local.SettingsRepository
import com.genius.snapscribe.data.repository.SessionRepository
import com.genius.snapscribe.data.repository.SnapRepository
import com.genius.snapscribe.domain.model.Session
import com.genius.snapscribe.domain.model.SnapCategory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val session: Session = Session.Guest,
    val snapCount: Int = 0,
    val folderCount: Int = SnapCategory.entries.size,
    val monthlyTotalRupiah: Long = 0L,
    val cacheSizeMb: Int = 142,
    val biometricLockEnabled: Boolean = false,
)

class ProfileViewModel(
    private val sessionRepository: SessionRepository,
    private val settingsRepository: SettingsRepository,
    private val snapRepository: SnapRepository,
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = combine(
        sessionRepository.session,
        settingsRepository.settings,
        snapRepository.observeSnaps(null),
    ) { session, settings, snaps ->
        ProfileUiState(
            session = session,
            snapCount = snaps.size,
            folderCount = SnapCategory.entries.size,
            monthlyTotalRupiah = snaps.sumOf { it.amountRupiah ?: 0L },
            cacheSizeMb = settings.cacheSizeMb,
            biometricLockEnabled = settings.biometricLockEnabled,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState(),
    )

    fun setBiometricLock(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setBiometricLock(enabled)
        }
    }

    fun signOut(onSignedOut: () -> Unit) {
        viewModelScope.launch {
            sessionRepository.signOut()
            settingsRepository.setOnboardingDone(false)
            onSignedOut()
        }
    }

    fun deleteLocalData(onDeleted: () -> Unit) {
        viewModelScope.launch {
            snapRepository.clearAllLocalData()
            settingsRepository.clearCache()
            onDeleted()
        }
    }
}
