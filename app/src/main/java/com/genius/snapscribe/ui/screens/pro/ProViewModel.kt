package com.genius.snapscribe.ui.screens.pro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genius.snapscribe.data.repository.SessionRepository
import com.genius.snapscribe.domain.model.BillingCycle
import com.genius.snapscribe.domain.model.Session
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ProCtaState {
    SIGN_IN_REQUIRED,
    START_FREE_TRIAL,
    SUBSCRIBE_DIRECT,
    ALREADY_PRO,
}

data class ProUiState(
    val session: Session = Session.Guest,
    val selectedCycle: BillingCycle = BillingCycle.YEARLY,
    val isLoading: Boolean = false,
    val ctaState: ProCtaState = ProCtaState.SIGN_IN_REQUIRED,
    val showTrialTimeline: Boolean = true,
)

sealed interface ProEvent {
    data object TrialStarted : ProEvent
    data object Subscribed : ProEvent
    data object Cancelled : ProEvent
}

class ProViewModel(
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val selectedCycle = MutableStateFlow(BillingCycle.YEARLY)
    private val isLoading = MutableStateFlow(false)

    private val _events = MutableSharedFlow<ProEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<ProEvent> = _events.asSharedFlow()

    val uiState: StateFlow<ProUiState> = combine(
        sessionRepository.session,
        selectedCycle,
        isLoading,
    ) { session, cycle, loading ->
        val ctaState = when (session) {
            Session.Guest -> ProCtaState.SIGN_IN_REQUIRED
            is Session.FreeAccount -> if (session.hasUsedTrial) {
                ProCtaState.SUBSCRIBE_DIRECT
            } else {
                ProCtaState.START_FREE_TRIAL
            }
            is Session.Pro -> ProCtaState.ALREADY_PRO
        }
        val showTimeline = session is Session.Guest ||
            (session is Session.FreeAccount && !session.hasUsedTrial)

        ProUiState(
            session = session,
            selectedCycle = cycle,
            isLoading = loading,
            ctaState = ctaState,
            showTrialTimeline = showTimeline,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProUiState(),
    )

    fun selectCycle(cycle: BillingCycle) {
        selectedCycle.value = cycle
    }

    fun startTrial() {
        viewModelScope.launch {
            isLoading.value = true
            sessionRepository.startTrial(selectedCycle.value)
            isLoading.value = false
            _events.tryEmit(ProEvent.TrialStarted)
        }
    }

    fun subscribe() {
        viewModelScope.launch {
            isLoading.value = true
            sessionRepository.subscribe(selectedCycle.value)
            isLoading.value = false
            _events.tryEmit(ProEvent.Subscribed)
        }
    }

    fun cancelSubscription() {
        viewModelScope.launch {
            isLoading.value = true
            sessionRepository.cancelSubscription()
            isLoading.value = false
            _events.tryEmit(ProEvent.Cancelled)
        }
    }
}
