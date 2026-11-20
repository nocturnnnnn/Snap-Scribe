package com.genius.snapscribe.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genius.snapscribe.data.repository.SnapRepository
import com.genius.snapscribe.domain.model.Snap
import com.genius.snapscribe.domain.model.SnapCategory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SnapDetailUiState(
    val snap: Snap? = null,
    val isLoaded: Boolean = false,
)

class SnapDetailViewModel(
    private val snapId: String,
    private val snapRepository: SnapRepository,
) : ViewModel() {

    val uiState: StateFlow<SnapDetailUiState> = snapRepository.observeSnap(snapId)
        .map { snap -> SnapDetailUiState(snap = snap, isLoaded = true) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SnapDetailUiState(),
        )

    fun updateCategory(category: SnapCategory) {
        viewModelScope.launch {
            snapRepository.updateCategory(snapId, category)
        }
    }

    fun deleteSnap(onDeleted: () -> Unit) {
        viewModelScope.launch {
            snapRepository.delete(snapId)
            onDeleted()
        }
    }
}
