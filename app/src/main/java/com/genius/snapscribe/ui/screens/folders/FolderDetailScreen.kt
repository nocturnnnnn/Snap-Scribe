package com.genius.snapscribe.ui.screens.folders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.genius.snapscribe.R
import com.genius.snapscribe.data.sample.SampleSnaps
import com.genius.snapscribe.domain.model.Snap
import com.genius.snapscribe.domain.model.SnapCategory
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.ui.components.FilterPillChip
import com.genius.snapscribe.ui.components.ProLockBadge
import com.genius.snapscribe.ui.components.SnapBackTopBar
import com.genius.snapscribe.ui.components.SnapImage
import com.genius.snapscribe.ui.components.SnapPrimaryButton
import com.genius.snapscribe.ui.components.SnapSearchField
import com.genius.snapscribe.ui.components.StatusBadge
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing
import com.genius.snapscribe.util.DateGroupKey
import com.genius.snapscribe.util.formatRelativeTimestamp
import com.genius.snapscribe.util.formatRupiah
import java.time.Instant

@Composable
fun FolderDetailRoute(
    viewModel: FolderDetailViewModel,
    onBack: () -> Unit,
    onSnapClick: (String) -> Unit,
    onCameraClick: () -> Unit,
    onProClick: (String) -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val categoryName = stringResource(uiState.category.labelRes)
    val exportDoneMsg = stringResource(R.string.folder_detail_export_done, categoryName)

    FolderDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onQueryChange = viewModel::onQueryChange,
        onSelectTag = viewModel::onSelectTag,
        onSnapClick = onSnapClick,
        onCameraClick = onCameraClick,
        onExportClick = {
            if (uiState.isPro) {
                onShowMessage(exportDoneMsg)
            } else {
                onProClick("export")
            }
        },
    )
}

@Composable
fun FolderDetailScreen(
    uiState: FolderDetailUiState,
    onBack: () -> Unit,
    onQueryChange: (String) -> Unit,
    onSelectTag: (String?) -> Unit,
    onSnapClick: (String) -> Unit,
    onCameraClick: () -> Unit,
    onExportClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = SnapScribeTheme.extendedColors
    val (catBg, catFg) = extended.categoryColors(uiState.category)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
    ) {
        SnapBackTopBar(
            title = stringResource(uiState.category.labelRes),
            onBack = onBack,
        )

        LazyColumn(
            contentPadding = PaddingValues(
                start = SnapSpacing.md,
                end = SnapSpacing.md,
                top = SnapSpacing.xs,
                bottom = 20.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
        ) {
            // Ledger Header Card
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    border = BorderStroke(1.dp, extended.cardBorder),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(SnapSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(uiState.category.descriptionRes),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusBadge(
                                text = pluralStringResource(
                                    R.plurals.folders_slips_count,
                                    uiState.totalInCategory,
                                    uiState.totalInCategory,
                                ),
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = catBg.copy(alpha = 0.55f),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    text = stringResource(
                                        if (uiState.category == SnapCategory.RECEIPTS) {
                                            R.string.folder_detail_total_label
                                        } else {
                                            R.string.folder_detail_count_label
                                        },
                                    ),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = catFg,
                                )
                                Text(
                                    text = if (uiState.category == SnapCategory.RECEIPTS) {
                                        formatRupiah(uiState.totalAmountRupiah)
                                    } else {
                                        pluralStringResource(
                                            R.plurals.folders_slips_count,
                                            uiState.totalInCategory,
                                            uiState.totalInCategory,
                                        )
                                    },
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = catFg,
                                )
                            }
                        }
                    }
                }
            }

            item {
                SnapSearchField(
                    query = uiState.query,
                    onQueryChange = onQueryChange,
                    placeholder = stringResource(R.string.folder_detail_search_hint),
                )
            }

            if (uiState.availableTags.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        FilterPillChip(
                            label = stringResource(R.string.category_all),
                            count = uiState.totalInCategory,
                            selected = uiState.selectedTag == null,
                            onClick = { onSelectTag(null) },
                        )
                        uiState.availableTags.forEach { (tag, count) ->
                            FilterPillChip(
                                label = tag,
                                count = count,
                                selected = uiState.selectedTag == tag,
                                onClick = { onSelectTag(tag) },
                            )
                        }
                    }
                }
            }

            uiState.groups.forEach { group ->
                item(key = "header_${group.key}") {
                    val label = when (group.key) {
                        DateGroupKey.Today -> stringResource(R.string.group_today)
                        DateGroupKey.Yesterday -> stringResource(R.string.group_yesterday)
                        is DateGroupKey.DateLabel -> group.key.formatted
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.semantics { heading() },
                        )
                    }
                }

                items(
                    items = group.snaps,
                    key = { it.id },
                ) { snap ->
                    FolderDetailItemCard(
                        snap = snap,
                        onClick = { onSnapClick(snap.id) },
                    )
                }
            }
        }

        // Bottom persistent bar: Export (Pro) + Add Photo
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column {
                HorizontalDivider(color = extended.cardBorder)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SnapSpacing.md, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        onClick = onExportClick,
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        border = BorderStroke(1.dp, extended.cardBorder),
                        modifier = Modifier.defaultMinSize(minHeight = SnapSpacing.ButtonHeight),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FileDownload,
                                contentDescription = stringResource(R.string.folder_detail_export_pro),
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp),
                            )
                            if (!uiState.isPro) {
                                ProLockBadge()
                            }
                        }
                    }

                    SnapPrimaryButton(
                        text = stringResource(R.string.folder_detail_add_photo),
                        leadingIcon = Icons.Outlined.PhotoCamera,
                        onClick = onCameraClick,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun FolderDetailItemCard(
    snap: Snap,
    onClick: () -> Unit,
) {
    val extended = SnapScribeTheme.extendedColors
    val todayLabel = stringResource(R.string.group_today)
    val yesterdayLabel = stringResource(R.string.group_yesterday)
    val dateLabel = snap.capturedAt.formatRelativeTimestamp(todayLabel, yesterdayLabel)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(SnapSpacing.CardRadius),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, extended.cardBorder),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                SnapImage(
                    image = snap.image,
                    contentDescription = snap.title,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(10.dp)),
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = snap.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = dateLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (snap.amountRupiah != null) {
                    Text(
                        text = formatRupiah(snap.amountRupiah),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DocumentScanner,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = snap.summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Verified,
                        contentDescription = null,
                        tint = extended.onSuccess,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = snap.merchantOrTag,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Text(
                    text = stringResource(R.string.folder_detail_view_slip),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FolderDetailPreview() {
    val snaps = SampleSnaps.buildDefaultSnaps(Instant.parse("2026-11-20T09:00:00Z"))
        .filter { it.category == SnapCategory.RECEIPTS }
    SnapScribeTheme(themeMode = ThemeMode.LIGHT) {
        FolderDetailScreen(
            uiState = FolderDetailUiState(
                category = SnapCategory.RECEIPTS,
                totalInCategory = snaps.size,
                totalAmountRupiah = snaps.sumOf { it.amountRupiah ?: 0L },
                availableTags = listOf("Superindo" to 2, "BCA" to 1),
                groups = listOf(SnapDateGroup(DateGroupKey.Today, snaps.take(2))),
            ),
            onBack = {},
            onQueryChange = {},
            onSelectTag = {},
            onSnapClick = {},
            onCameraClick = {},
            onExportClick = {},
        )
    }
}
