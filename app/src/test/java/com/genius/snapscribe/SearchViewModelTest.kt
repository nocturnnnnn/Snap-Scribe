package com.genius.snapscribe

import app.cash.turbine.test
import com.genius.snapscribe.data.repository.FakeSessionRepository
import com.genius.snapscribe.data.repository.FakeSnapRepository
import com.genius.snapscribe.data.sample.SampleSnaps
import com.genius.snapscribe.domain.model.SnapCategory
import com.genius.snapscribe.ui.screens.search.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @Test
    fun debouncedSearch_filtersAfterDelay_andTracksHistory() = runTest(testDispatcher) {
        val snapRepo = FakeSnapRepository(SampleSnaps.buildDefaultSnaps(Instant.parse("2026-11-20T09:00:00Z")))
        val settingsRepo = InMemorySettingsRepository()
        val sessionRepo = FakeSessionRepository(delayMs = 0L)
        val viewModel = SearchViewModel(
            snapRepository = snapRepo,
            settingsRepository = settingsRepo,
            sessionRepository = sessionRepo,
            debounceMs = 250L,
        )

        viewModel.uiState.test {
            awaitItem() // initial
            advanceUntilIdle()
            val loaded = awaitItem()
            assertEquals(20, loaded.results.size)

            viewModel.onQueryChange("BCA")
            // Immediately after typing (before 250ms), query updates in UI state
            assertEquals("BCA", awaitItem().query)

            advanceTimeBy(260L)
            advanceUntilIdle()
            val searchResult = expectMostRecentItem()
            assertTrue(searchResult.results.isNotEmpty())
            assertTrue(searchResult.results.any { it.snap.title.contains("BCA") })

            viewModel.onSelectCategory(SnapCategory.NOTES)
            advanceUntilIdle()
            val notesFiltered = expectMostRecentItem()
            assertTrue(notesFiltered.results.all { it.snap.category == SnapCategory.NOTES })

            viewModel.onSearchSubmit()
            advanceUntilIdle()
            val withHistory = expectMostRecentItem()
            assertEquals("BCA", withHistory.recentSearches.first())

            viewModel.onClearHistory()
            advanceUntilIdle()
            val cleared = expectMostRecentItem()
            assertTrue(cleared.recentSearches.isEmpty())
        }
    }
}
