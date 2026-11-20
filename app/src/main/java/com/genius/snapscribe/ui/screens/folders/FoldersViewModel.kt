package com.genius.snapscribe.ui.screens.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genius.snapscribe.data.repository.CategoryStat
import com.genius.snapscribe.data.repository.SessionRepository
import com.genius.snapscribe.data.repository.SnapRepository
import com.genius.snapscribe.domain.model.Session
import com.genius.snapscribe.domain.model.Snap
import com.genius.snapscribe.domain.model.SnapCategory
import com.genius.snapscribe.util.DateGroupKey
import com.genius.snapscribe.util.toDateGroupKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CustomCollectionPreview(
    val id: String,
    val title: String,
    val subtitle: String,
    val count: Int,
)

data class FoldersUiState(
    val totalSlips: Int = 0,
    val smartFolders: List<CategoryStat> = emptyList(),
    val filterQuery: String = "",
    val isPro: Boolean = false,
    val customCollections: List<CustomCollectionPreview> = emptyList(),
    val userInitials: String? = null,
    val isSignedIn: Boolean = false,
)

class FoldersViewModel(
    snapRepository: SnapRepository,
    sessionRepository: SessionRepository,
) : ViewModel() {

    private val filterQuery = MutableStateFlow("")

    val uiState: StateFlow<FoldersUiState> = combine(
        snapRepository.observeCategoryStats(),
        snapRepository.observeSnaps(null),
        sessionRepository.session,
        filterQuery,
    ) { stats, allSnaps, session, query ->
        val clean = query.trim().lowercase()
        val orderedStats = SnapCategory.entries.mapNotNull { stats[it] }.filter { stat ->
            clean.isEmpty() ||
                stat.category.name.lowercase().contains(clean) ||
                stat.latestSummary.lowercase().contains(clean) ||
                stat.topTags.any { it.lowercase().contains(clean) }
        }
        val initials = when (session) {
            Session.Guest -> null
            is Session.FreeAccount -> session.user.initials
            is Session.Pro -> session.user.initials
        }
        FoldersUiState(
            totalSlips = allSnaps.size,
            smartFolders = orderedStats,
            filterQuery = query,
            isPro = session is Session.Pro,
            customCollections = listOf(
                CustomCollectionPreview("c1", "Renovasi Dapur", "Nota material & tukang", 14),
                CustomCollectionPreview("c2", "Reimburse Kantor Q4", "Struk makan & transportasi", 9),
                CustomCollectionPreview("c3", "Daftar Obat & Resep", "Apotek & jadwal kontrol", 6),
            ),
            userInitials = initials,
            isSignedIn = session !is Session.Guest,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FoldersUiState(),
    )

    fun onFilterQueryChange(value: String) {
        filterQuery.value = value
    }
}

data class SnapDateGroup(
    val key: DateGroupKey,
    val snaps: List<Snap>,
)

data class FolderDetailUiState(
    val category: SnapCategory,
    val totalInCategory: Int = 0,
    val totalAmountRupiah: Long = 0L,
    val latestHighlight: String = "",
    val availableTags: List<Pair<String, Int>> = emptyList(),
    val selectedTag: String? = null,
    val query: String = "",
    val groups: List<SnapDateGroup> = emptyList(),
    val isPro: Boolean = false,
)

class FolderDetailViewModel(
    private val category: SnapCategory,
    snapRepository: SnapRepository,
    sessionRepository: SessionRepository,
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")
    private val selectedTagFlow = MutableStateFlow<String?>(null)

    val uiState: StateFlow<FolderDetailUiState> = combine(
        snapRepository.observeSnaps(category),
        queryFlow,
        selectedTagFlow,
        sessionRepository.session,
    ) { categorySnaps, query, selectedTag, session ->
        val tags = categorySnaps
            .groupBy { it.merchantOrTag }
            .filterKeys { it.isNotBlank() }
            .map { (tag, list) -> tag to list.size }
            .sortedByDescending { it.second }

        val cleanQuery = query.trim().lowercase()
        val filtered = categorySnaps.filter { snap ->
            val tagOk = selectedTag == null || snap.merchantOrTag == selectedTag
            val queryOk = cleanQuery.isEmpty() ||
                snap.title.lowercase().contains(cleanQuery) ||
                snap.summary.lowercase().contains(cleanQuery) ||
                snap.merchantOrTag.lowercase().contains(cleanQuery) ||
                snap.fullText.any { it.lowercase().contains(cleanQuery) }
            tagOk && queryOk
        }

        val grouped = filtered
            .groupBy { it.capturedAt.toDateGroupKey() }
            .map { (key, snaps) -> SnapDateGroup(key = key, snaps = snaps) }

        FolderDetailUiState(
            category = category,
            totalInCategory = categorySnaps.size,
            totalAmountRupiah = categorySnaps.sumOf { it.amountRupiah ?: 0L },
            latestHighlight = categorySnaps.firstOrNull()?.title.orEmpty(),
            availableTags = tags,
            selectedTag = selectedTag,
            query = query,
            groups = grouped,
            isPro = session is Session.Pro,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FolderDetailUiState(category = category),
    )

    fun onQueryChange(value: String) {
        queryFlow.value = value
    }

    fun onSelectTag(tag: String?) {
        selectedTagFlow.value = tag
    }
}
