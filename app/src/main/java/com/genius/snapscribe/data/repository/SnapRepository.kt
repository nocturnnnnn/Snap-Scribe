package com.genius.snapscribe.data.repository

import com.genius.snapscribe.domain.model.Snap
import com.genius.snapscribe.domain.model.SnapCategory
import kotlinx.coroutines.flow.Flow

data class SearchHit(
    val snap: Snap,
    val matchedSnippet: String,
    val matchRanges: List<IntRange>,
)

data class CategoryStat(
    val category: SnapCategory,
    val count: Int,
    val totalAmountRupiah: Long,
    val latestSummary: String,
    val topTags: List<String>,
)

interface SnapRepository {
    fun observeSnaps(category: SnapCategory? = null): Flow<List<Snap>>
    fun search(query: String, category: SnapCategory? = null): Flow<List<SearchHit>>
    fun observeSnap(id: String): Flow<Snap?>
    fun observeCategoryStats(): Flow<Map<SnapCategory, CategoryStat>>
    suspend fun updateCategory(id: String, category: SnapCategory)
    suspend fun delete(id: String)
    suspend fun addSnap(snap: Snap)
    suspend fun clearAllLocalData()
}
