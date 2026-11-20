package com.genius.snapscribe.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genius.snapscribe.data.repository.SessionRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AuthErrorField {
    NAME,
    EMAIL,
    PASSWORD,
    CONFIRM_PASSWORD,
}

data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val rememberMe: Boolean = true,
    val isLoading: Boolean = false,
    val errors: Set<AuthErrorField> = emptySet(),
)

sealed interface AuthEvent {
    data object Authenticated : AuthEvent
}

class AuthViewModel(
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<AuthEvent> = _events.asSharedFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, errors = it.errors - AuthErrorField.NAME) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errors = it.errors - AuthErrorField.EMAIL) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                errors = it.errors - AuthErrorField.PASSWORD - AuthErrorField.CONFIRM_PASSWORD,
            )
        }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update {
            it.copy(confirmPassword = value, errors = it.errors - AuthErrorField.CONFIRM_PASSWORD)
        }
    }

    fun onRememberMeChange(checked: Boolean) {
        _uiState.update { it.copy(rememberMe = checked) }
    }

    fun signIn() {
        val state = _uiState.value
        val errors = buildSet {
            if (!isValidEmail(state.email)) add(AuthErrorField.EMAIL)
            if (state.password.length < 6) add(AuthErrorField.PASSWORD)
        }
        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(errors = errors) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errors = emptySet()) }
            sessionRepository.signIn(state.email, state.password)
            _uiState.update { it.copy(isLoading = false) }
            _events.tryEmit(AuthEvent.Authenticated)
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errors = emptySet()) }
            sessionRepository.signInWithGoogle()
            _uiState.update { it.copy(isLoading = false) }
            _events.tryEmit(AuthEvent.Authenticated)
        }
    }

    fun register() {
        val state = _uiState.value
        val errors = buildSet {
            if (state.name.trim().length < 2) add(AuthErrorField.NAME)
            if (!isValidEmail(state.email)) add(AuthErrorField.EMAIL)
            if (state.password.length < 6) add(AuthErrorField.PASSWORD)
            if (state.confirmPassword != state.password) add(AuthErrorField.CONFIRM_PASSWORD)
        }
        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(errors = errors) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errors = emptySet()) }
            sessionRepository.register(state.name, state.email, state.password)
            _uiState.update { it.copy(isLoading = false) }
            _events.tryEmit(AuthEvent.Authenticated)
        }
    }

    fun continueOffline() {
        viewModelScope.launch {
            sessionRepository.continueOffline()
            _events.tryEmit(AuthEvent.Authenticated)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val trimmed = email.trim()
        val atIndex = trimmed.indexOf('@')
        val dotIndex = trimmed.lastIndexOf('.')
        return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < trimmed.length - 1
    }
}
