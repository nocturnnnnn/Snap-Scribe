package com.genius.snapscribe

import com.genius.snapscribe.data.repository.FakeSnapRepository
import com.genius.snapscribe.data.sample.SampleSnaps
import com.genius.snapscribe.domain.model.SnapCategory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class FakeSnapRepositoryTest {

    private val fixedNow = Instant.parse("2026-11-20T09:00:00Z")

    @Test
    fun observeSnaps_filtersByCategory() = runTest {
        val repo = FakeSnapRepository(SampleSnaps.buildDefaultSnaps(fixedNow))

        val all = repo.observeSnaps(null).first()
        val receipts = repo.observeSnaps(SnapCategory.RECEIPTS).first()
        val codes = repo.observeSnaps(SnapCategory.CODES_LINKS).first()

        assertEquals(20, all.size)
        assertEquals(7, receipts.size)
        assertTrue(receipts.all { it.category == SnapCategory.RECEIPTS })
        assertEquals(5, codes.size)
    }

    @Test
    fun search_matchesCaseInsensitivelyAndComputesRanges() = runTest {
        val repo = FakeSnapRepository(SampleSnaps.buildDefaultSnaps(fixedNow))

        val hits = repo.search("superindo", category = null).first()

        assertTrue(hits.isNotEmpty())
        assertTrue(hits.all { it.snap.title.contains("Superindo", ignoreCase = true) ||
            it.matchedSnippet.contains("Superindo", ignoreCase = true) })
        val firstHit = hits.first()
        assertTrue(firstHit.matchRanges.isNotEmpty())
        val range = firstHit.matchRanges.first()
        assertEquals("superindo", firstHit.matchedSnippet.substring(range).lowercase())
    }

    @Test
    fun updateCategory_movesSnapToNewCategory() = runTest {
        val repo = FakeSnapRepository(SampleSnaps.buildDefaultSnaps(fixedNow))

        repo.updateCategory("snap-01", SnapCategory.NOTES)
        val updated = repo.observeSnap("snap-01").first()

        assertNotNull(updated)
        assertEquals(SnapCategory.NOTES, updated?.category)
    }

    @Test
    fun delete_removesSnapFromRepository() = runTest {
        val repo = FakeSnapRepository(SampleSnaps.buildDefaultSnaps(fixedNow))

        repo.delete("snap-01")
        val after = repo.observeSnap("snap-01").first()
        val all = repo.observeSnaps(null).first()

        assertNull(after)
        assertEquals(19, all.size)
    }

    @Test
    fun observeCategoryStats_sumsReceiptTotals() = runTest {
        val repo = FakeSnapRepository(SampleSnaps.buildDefaultSnaps(fixedNow))

        val stats = repo.observeCategoryStats().first()
        val receiptsStat = stats[SnapCategory.RECEIPTS]

        assertNotNull(receiptsStat)
        assertEquals(7, receiptsStat?.count)
        assertEquals(1_448_500L, receiptsStat?.totalAmountRupiah)
    }
}
