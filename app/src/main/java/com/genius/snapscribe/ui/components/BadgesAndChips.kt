package com.genius.snapscribe.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.genius.snapscribe.R
import com.genius.snapscribe.domain.model.SnapCategory
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing

@Composable
fun CategoryPill(
    category: SnapCategory,
    modifier: Modifier = Modifier,
    editable: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val (bg, fg) = SnapScribeTheme.extendedColors.categoryColors(category)
    val label = stringResource(category.labelRes)
    if (onClick != null) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(50),
            color = bg,
            modifier = modifier.defaultMinSize(minHeight = SnapSpacing.MinTouchTarget),
        ) {
            CategoryPillBody(label = label, fg = fg, editable = editable, verticalPad = 10.dp)
        }
    } else {
        Surface(
            shape = RoundedCornerShape(50),
            color = bg,
            modifier = modifier,
        ) {
            CategoryPillBody(label = label, fg = fg, editable = editable, verticalPad = 5.dp)
        }
    }
}

@Composable
private fun CategoryPillBody(
    label: String,
    fg: Color,
    editable: Boolean,
    verticalPad: androidx.compose.ui.unit.Dp,
) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = verticalPad),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = fg,
            maxLines = 1,
        )
        if (editable) {
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                tint = fg,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = SnapScribeTheme.extendedColors.successContainer,
    contentColor: Color = SnapScribeTheme.extendedColors.onSuccess,
    dotColor: Color = contentColor,
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = containerColor,
        modifier = modifier.defaultMinSize(minHeight = 32.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(dotColor),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun ProLockBadge(
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = stringResource(R.string.cd_pro_locked),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(12.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.folders_pro_badge),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
fun CategoryFilterRow(
    selectedCategory: SnapCategory?,
    totalCount: Int,
    counts: Map<SnapCategory, Int>,
    onSelectCategory: (SnapCategory?) -> Unit,
    modifier: Modifier = Modifier,
    showCounts: Boolean = true,
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = SnapSpacing.md),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterPillChip(
            label = stringResource(R.string.category_all),
            count = if (showCounts) totalCount else null,
            icon = Icons.Outlined.AutoAwesome,
            selected = selectedCategory == null,
            onClick = { onSelectCategory(null) },
        )

        SnapCategory.entries.forEach { category ->
            FilterPillChip(
                label = stringResource(category.labelRes),
                count = if (showCounts) (counts[category] ?: 0) else null,
                icon = category.icon,
                selected = selectedCategory == category,
                onClick = { onSelectCategory(category) },
            )
        }
    }
}

@Composable
fun FilterPillChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    count: Int? = null,
    icon: ImageVector? = null,
) {
    val extended = SnapScribeTheme.extendedColors
    val bg = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceContainerLowest
    }
    val fg = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val border = if (selected) null else BorderStroke(1.dp, extended.cardBorder)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = bg,
        border = border,
        modifier = modifier.defaultMinSize(minHeight = SnapSpacing.MinTouchTarget),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = fg,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = fg,
                maxLines = 1,
            )
            if (count != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (selected) {
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    },
                ) {
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = fg,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                    )
                }
            }
        }
    }
}
