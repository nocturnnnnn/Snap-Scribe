package com.genius.snapscribe

import com.genius.snapscribe.domain.model.DetectedEntity
import com.genius.snapscribe.domain.model.EntityType
import com.genius.snapscribe.domain.model.ImageSource
import com.genius.snapscribe.domain.model.QuickActionType
import com.genius.snapscribe.domain.model.Snap
import com.genius.snapscribe.domain.model.SnapCategory
import com.genius.snapscribe.domain.model.SnapSource
import com.genius.snapscribe.domain.model.primaryAction
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class QuickActionTest {

    private fun snapWith(
        entities: List<DetectedEntity>,
        fullText: List<String> = listOf("Line 1", "Line 2"),
    ) = Snap(
        id = "test",
        image = ImageSource.Drawable(0),
        category = SnapCategory.NOTES,
        title = "Title",
        summary = "Summary",
        merchantOrTag = "Tag",
        capturedAt = Instant.EPOCH,
        source = SnapSource.SCREENSHOTS,
        fullText = fullText,
        entities = entities,
        blocks = emptyList(),
    )

    @Test
    fun primaryAction_mapsEveryEntityType() {
        assertEquals(
            QuickActionType.COPY_AMOUNT,
            snapWith(listOf(DetectedEntity(EntityType.AMOUNT, "Rp 50.000"))).primaryAction().type,
        )
        assertEquals(
            QuickActionType.COPY_PASSWORD,
            snapWith(listOf(DetectedEntity(EntityType.PASSWORD, "secret123"))).primaryAction().type,
        )
        assertEquals(
            QuickActionType.COPY_RESI,
            snapWith(listOf(DetectedEntity(EntityType.TRACKING_NUMBER, "JNE123"))).primaryAction().type,
        )
        assertEquals(
            QuickActionType.OPEN_MAPS,
            snapWith(listOf(DetectedEntity(EntityType.ADDRESS, "Jl. Sudirman"))).primaryAction().type,
        )
        assertEquals(
            QuickActionType.OPEN_LINK,
            snapWith(listOf(DetectedEntity(EntityType.URL, "https://example.com"))).primaryAction().type,
        )
        assertEquals(
            QuickActionType.ADD_CALENDAR,
            snapWith(listOf(DetectedEntity(EntityType.DATE, "Kamis 16:00"))).primaryAction().type,
        )

        val fallback = snapWith(emptyList(), fullText = listOf("Cat A", "Cat B")).primaryAction()
        assertEquals(QuickActionType.COPY_TEXT, fallback.type)
        assertEquals("Cat A\nCat B", fallback.payload)
    }
}
