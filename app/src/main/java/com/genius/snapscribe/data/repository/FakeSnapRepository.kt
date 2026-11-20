package com.genius.snapscribe.data.repository

import com.genius.snapscribe.data.sample.SampleSnaps
import com.genius.snapscribe.domain.model.Snap
import com.genius.snapscribe.domain.model.SnapCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeSnapRepository(
    initialSnaps: List<Snap> = SampleSnaps.buildDefaultSnaps(),
) : SnapRepository {

    private val snapsFlow = MutableStateFlow(initialSnaps)

    override fun observeSnaps(category: SnapCategory?): Flow<List<Snap>> =
        snapsFlow.map { snaps ->
            val filtered = if (category == null) snaps else snaps.filter { it.category == category }
            filtered.sortedByDescending { it.capturedAt }
        }

    override fun search(query: String, category: SnapCategory?): Flow<List<SearchHit>> =
        snapsFlow.map { snaps ->
            val trimmed = query.trim()
            val byCategory = if (category == null) snaps else snaps.filter { it.category == category }
            val sorted = byCategory.sortedByDescending { it.capturedAt }
            if (trimmed.isEmpty()) {
                sorted.map { snap ->
                    SearchHit(
                        snap = snap,
                        matchedSnippet = snap.fullText.firstOrNull() ?: snap.summary,
                        matchRanges = emptyList(),
                    )
                }
            } else {
                sorted.mapNotNull { snap -> matchSnap(snap, trimmed) }
            }
        }

    override fun observeSnap(id: String): Flow<Snap?> =
        snapsFlow.map { snaps -> snaps.firstOrNull { it.id == id } }

    override fun observeCategoryStats(): Flow<Map<SnapCategory, CategoryStat>> =
        snapsFlow.map { snaps ->
            SnapCategory.entries.associateWith { category ->
                val matching = snaps
                    .filter { it.category == category }
                    .sortedByDescending { it.capturedAt }
                CategoryStat(
                    category = category,
                    count = matching.size,
                    totalAmountRupiah = matching.sumOf { it.amountRupiah ?: 0L },
                    latestSummary = matching.firstOrNull()?.title.orEmpty(),
                    topTags = matching
                        .map { it.merchantOrTag }
                        .filter { it.isNotBlank() }
                        .distinct()
                        .take(3),
                )
            }
        }

    override suspend fun updateCategory(id: String, category: SnapCategory) {
        snapsFlow.update { snaps ->
            snaps.map { snap ->
                if (snap.id == id) snap.copy(category = category) else snap
            }
        }
    }

    override suspend fun delete(id: String) {
        snapsFlow.update { snaps -> snaps.filterNot { it.id == id } }
    }

    override suspend fun addSnap(snap: Snap) {
        snapsFlow.update { snaps -> listOf(snap) + snaps }
    }

    override suspend fun clearAllLocalData() {
        snapsFlow.value = emptyList()
    }

    private fun matchSnap(snap: Snap, query: String): SearchHit? {
        val candidateLines = buildList {
            addAll(snap.fullText)
            add(snap.title)
            add(snap.summary)
            add(snap.merchantOrTag)
            addAll(snap.entities.map { it.value })
        }
        val combinedText = snap.fullText.joinToString(" • ").ifBlank { snap.summary }
        val lowerQuery = query.lowercase()

        val matchedInTitleOrMeta = listOf(snap.title, snap.summary, snap.merchantOrTag)
            .any { it.contains(lowerQuery, ignoreCase = true) } ||
            snap.entities.any { it.value.contains(lowerQuery, ignoreCase = true) }

        val bestLine = candidateLines.firstOrNull { it.contains(lowerQuery, ignoreCase = true) }
        if (!matchedInTitleOrMeta && bestLine == null) return null

        val snippet = if (combinedText.contains(lowerQuery, ignoreCase = true)) {
            combinedText
        } else {
            bestLine ?: combinedText
        }
        val ranges = findMatchRanges(snippet, query)
        return SearchHit(
            snap = snap,
            matchedSnippet = snippet,
            matchRanges = ranges,
        )
    }

    private fun findMatchRanges(text: String, query: String): List<IntRange> {
        if (query.isEmpty()) return emptyList()
        val ranges = mutableListOf<IntRange>()
        var startIndex = 0
        while (startIndex < text.length) {
            val found = text.indexOf(query, startIndex = startIndex, ignoreCase = true)
            if (found < 0) break
            ranges += found until (found + query.length)
            startIndex = found + query.length
        }
        return ranges
    }
}
