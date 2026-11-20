package com.genius.snapscribe.domain.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.ui.graphics.vector.ImageVector
import com.genius.snapscribe.R

enum class QuickActionType(
    @param:StringRes val labelRes: Int,
    val icon: ImageVector,
    val isClipboardAction: Boolean,
    val isSensitive: Boolean = false,
) {
    COPY_AMOUNT(
        labelRes = R.string.action_copy_amount,
        icon = Icons.Outlined.ContentCopy,
        isClipboardAction = true,
    ),
    COPY_PASSWORD(
        labelRes = R.string.action_copy_password,
        icon = Icons.Outlined.Key,
        isClipboardAction = true,
        isSensitive = true,
    ),
    COPY_RESI(
        labelRes = R.string.action_copy_resi,
        icon = Icons.Outlined.LocalShipping,
        isClipboardAction = true,
    ),
    OPEN_MAPS(
        labelRes = R.string.action_open_maps,
        icon = Icons.Outlined.NearMe,
        isClipboardAction = false,
    ),
    OPEN_LINK(
        labelRes = R.string.action_open_link,
        icon = Icons.Outlined.OpenInBrowser,
        isClipboardAction = false,
    ),
    ADD_CALENDAR(
        labelRes = R.string.action_add_calendar,
        icon = Icons.Outlined.Event,
        isClipboardAction = false,
    ),
    COPY_TEXT(
        labelRes = R.string.action_copy_text,
        icon = Icons.Outlined.ContentCopy,
        isClipboardAction = true,
    ),
}

data class QuickAction(
    val type: QuickActionType,
    val payload: String,
)

fun Snap.primaryAction(): QuickAction {
    val firstEntity = entities.firstOrNull()
    val type = when (firstEntity?.type) {
        EntityType.AMOUNT -> QuickActionType.COPY_AMOUNT
        EntityType.PASSWORD -> QuickActionType.COPY_PASSWORD
        EntityType.TRACKING_NUMBER -> QuickActionType.COPY_RESI
        EntityType.ADDRESS -> QuickActionType.OPEN_MAPS
        EntityType.URL -> QuickActionType.OPEN_LINK
        EntityType.DATE -> QuickActionType.ADD_CALENDAR
        EntityType.OTP -> QuickActionType.COPY_PASSWORD
        EntityType.ACCOUNT_NUMBER, EntityType.PHONE, null -> QuickActionType.COPY_TEXT
    }
    val payload = firstEntity?.value ?: fullText.joinToString(separator = "\n")
    return QuickAction(type = type, payload = payload)
}
