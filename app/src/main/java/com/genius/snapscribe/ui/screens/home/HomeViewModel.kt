package com.genius.snapscribe.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genius.snapscribe.data.repository.SessionRepository
import com.genius.snapscribe.data.repository.SnapRepository
import com.genius.snapscribe.domain.model.Session
import com.genius.snapscribe.domain.model.Snap
import com.genius.snapscribe.domain.model.SnapCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class SnapSortOrder {
    NEWEST,
    OLDEST,
}

data class HomeUiState(
    val snaps: List<Snap> = emptyList(),
    val totalCount: Int = 0,
    val categoryCounts: Map<SnapCategory, Int> = emptyMap(),
    val selectedCategory: SnapCategory? = null,
    val sortOrder: SnapSortOrder = SnapSortOrder.NEWEST,
    val userInitials: String? = null,
    val isSignedIn: Boolean = false,
)

class HomeViewModel(
    snapRepository: SnapRepository,
    sessionRepository: SessionRepository,
) : ViewModel() {

    private val selectedCategory = MutableStateFlow<SnapCategory?>(null)
    private val sortOrder = MutableStateFlow(SnapSortOrder.NEWEST)

    val uiState: StateFlow<HomeUiState> = combine(
        snapRepository.observeSnaps(category = null),
        snapRepository.observeCategoryStats(),
        selectedCategory,
        sortOrder,
        sessionRepository.session,
    ) { allSnaps, stats, category, order, session ->
        val filtered = if (category == null) {
            allSnaps
        } else {
            allSnaps.filter { it.category == category }
        }
        val sorted = when (order) {
            SnapSortOrder.NEWEST -> filtered.sortedByDescending { it.capturedAt }
            SnapSortOrder.OLDEST -> filtered.sortedBy { it.capturedAt }
        }
        val initials = when (session) {
            Session.Guest -> null
            is Session.FreeAccount -> session.user.initials
            is Session.Pro -> session.user.initials
        }
        HomeUiState(
            snaps = sorted,
            totalCount = allSnaps.size,
            categoryCounts = stats.mapValues { (_, stat) -> stat.count },
            selectedCategory = category,
            sortOrder = order,
            userInitials = initials,
            isSignedIn = session !is Session.Guest,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(),
    )

    fun selectCategory(category: SnapCategory?) {
        selectedCategory.value = category
    }

    fun setSortOrder(order: SnapSortOrder) {
        sortOrder.value = order
    }
}
