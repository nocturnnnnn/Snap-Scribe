package com.genius.snapscribe.domain.model

import androidx.annotation.DrawableRes
import java.time.Instant

enum class SnapSource {
    SCREENSHOTS,
    WHATSAPP,
    CAMERA,
    SCANNED,
}

enum class EntityType {
    AMOUNT,
    PASSWORD,
    TRACKING_NUMBER,
    URL,
    ADDRESS,
    DATE,
    ACCOUNT_NUMBER,
    OTP,
    PHONE,
}

data class DetectedEntity(
    val type: EntityType,
    val value: String,
    val label: String? = null,
)

/**
 * Pure Kotlin normalized bounding box (0f..1f relative to image dimensions)
 * so domain models stay free of Android framework graphics dependencies in JVM unit tests.
 */
data class NormalizedRect(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
)

data class OcrBlock(
    val text: String,
    val box: NormalizedRect,
)

sealed interface ImageSource {
    data class Drawable(@param:DrawableRes val resId: Int) : ImageSource
    data class Uri(val uriString: String) : ImageSource
}

data class Snap(
    val id: String,
    val image: ImageSource,
    val category: SnapCategory,
    val title: String,
    val summary: String,
    val merchantOrTag: String,
    val capturedAt: Instant,
    val source: SnapSource,
    val fullText: List<String>,
    val entities: List<DetectedEntity>,
    val blocks: List<OcrBlock>,
    val amountRupiah: Long? = null,
    val verified: Boolean = true,
)
