package com.genius.snapscribe.domain.model

import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.ui.graphics.vector.ImageVector
import com.genius.snapscribe.R
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class SnapCategory(
    @param:StringRes val labelRes: Int,
    @param:StringRes val descriptionRes: Int,
    val icon: ImageVector,
) {
    RECEIPTS(
        labelRes = R.string.category_receipts,
        descriptionRes = R.string.folder_desc_receipts,
        icon = Icons.AutoMirrored.Outlined.ReceiptLong,
    ),
    CODES_LINKS(
        labelRes = R.string.category_codes_links,
        descriptionRes = R.string.folder_desc_codes_links,
        icon = Icons.Outlined.VpnKey,
    ),
    ADDRESSES(
        labelRes = R.string.category_addresses,
        descriptionRes = R.string.folder_desc_addresses,
        icon = Icons.Outlined.LocationOn,
    ),
    NOTES(
        labelRes = R.string.category_notes,
        descriptionRes = R.string.folder_desc_notes,
        icon = Icons.Outlined.Description,
    ),
}
