package com.genius.snapscribe.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genius.snapscribe.data.local.SettingsRepository
import com.genius.snapscribe.data.repository.SearchHit
import com.genius.snapscribe.data.repository.SessionRepository
import com.genius.snapscribe.data.repository.SnapRepository
import com.genius.snapscribe.domain.model.Session
import com.genius.snapscribe.domain.model.SnapCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val selectedCategory: SnapCategory? = null,
    val recentSearches: List<String> = emptyList(),
    val results: List<SearchHit> = emptyList(),
    val userInitials: String? = null,
    val isSignedIn: Boolean = false,
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val snapRepository: SnapRepository,
    private val settingsRepository: SettingsRepository,
    sessionRepository: SessionRepository,
    initialQuery: String? = null,
    private val debounceMs: Long = 250L,
) : ViewModel() {

    private val queryFlow = MutableStateFlow(initialQuery.orEmpty())
    private val selectedCategoryFlow = MutableStateFlow<SnapCategory?>(null)

    private val debouncedQuery = if (debounceMs > 0L) {
        queryFlow.debounce(debounceMs)
    } else {
        queryFlow
    }

    private val resultsFlow = combine(
        debouncedQuery,
        selectedCategoryFlow,
    ) { query, category ->
        query to category
    }.flatMapLatest { (query, category) ->
        snapRepository.search(query, category)
    }

    val uiState: StateFlow<SearchUiState> = combine(
        queryFlow,
        selectedCategoryFlow,
        settingsRepository.settings,
        resultsFlow,
        sessionRepository.session,
    ) { query, category, settings, results, session ->
        val initials = when (session) {
            Session.Guest -> null
            is Session.FreeAccount -> session.user.initials
            is Session.Pro -> session.user.initials
        }
        SearchUiState(
            query = query,
            selectedCategory = category,
            recentSearches = settings.recentSearches,
            results = results,
            userInitials = initials,
            isSignedIn = session !is Session.Guest,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchUiState(query = initialQuery.orEmpty()),
    )

    fun onQueryChange(newQuery: String) {
        queryFlow.value = newQuery
    }

    fun applyExternalQuery(query: String?) {
        if (!query.isNullOrBlank()) {
            queryFlow.value = query
            viewModelScope.launch {
                settingsRepository.addRecentSearch(query)
            }
        }
    }

    fun onSearchSubmit() {
        val current = queryFlow.value.trim()
        if (current.isNotEmpty()) {
            viewModelScope.launch {
                settingsRepository.addRecentSearch(current)
            }
        }
    }

    fun onSelectRecentSearch(term: String) {
        queryFlow.value = term
        viewModelScope.launch {
            settingsRepository.addRecentSearch(term)
        }
    }

    fun onClearHistory() {
        viewModelScope.launch {
            settingsRepository.clearRecentSearches()
        }
    }

    fun onSelectCategory(category: SnapCategory?) {
        selectedCategoryFlow.value = category
    }
}
