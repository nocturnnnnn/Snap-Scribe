package com.genius.snapscribe.ui.screens.folders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.genius.snapscribe.data.repository.CategoryStat
import com.genius.snapscribe.domain.model.SnapCategory
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.ui.components.InfoTipCard
import com.genius.snapscribe.ui.components.ProLockBadge
import com.genius.snapscribe.ui.components.ProUpsellCard
import com.genius.snapscribe.ui.components.SectionHeader
import com.genius.snapscribe.ui.components.SnapBrandTopBar
import com.genius.snapscribe.ui.components.SnapSearchField
import com.genius.snapscribe.ui.components.StatusBadge
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing
import com.genius.snapscribe.util.formatRupiah

@Composable
fun FoldersRoute(
    viewModel: FoldersViewModel,
    onFolderClick: (SnapCategory) -> Unit,
    onProfileClick: () -> Unit,
    onProClick: (String) -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val proFolderMessage = stringResource(R.string.folders_my_collections)

    FoldersScreen(
        uiState = uiState,
        onFilterQueryChange = viewModel::onFilterQueryChange,
        onFolderClick = onFolderClick,
        onProfileClick = onProfileClick,
        onAddFolderClick = {
            if (uiState.isPro) {
                onShowMessage(proFolderMessage)
            } else {
                onProClick("folders")
            }
        },
        onProUpsellClick = { onProClick("folders") },
    )
}

@Composable
fun FoldersScreen(
    uiState: FoldersUiState,
    onFilterQueryChange: (String) -> Unit,
    onFolderClick: (SnapCategory) -> Unit,
    onProfileClick: () -> Unit,
    onAddFolderClick: () -> Unit,
    onProUpsellClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = SnapScribeTheme.extendedColors

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        SnapBrandTopBar(
            subtitle = stringResource(R.string.nav_folders),
            userInitials = uiState.userInitials,
            isSignedIn = uiState.isSignedIn,
            onProfileClick = onProfileClick,
        )

        LazyColumn(
            contentPadding = PaddingValues(
                start = SnapSpacing.md,
                end = SnapSpacing.md,
                top = SnapSpacing.xs,
                bottom = SnapSpacing.BottomNavClearance,
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.folders_title),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.semantics { heading() },
                        )
                        Text(
                            text = stringResource(R.string.folders_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    OutlinedButton(
                        onClick = onAddFolderClick,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                        modifier = Modifier.defaultMinSize(minHeight = SnapSpacing.MinTouchTarget),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CreateNewFolder,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.folders_add_button),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }

            item {
                SnapSearchField(
                    query = uiState.filterQuery,
                    onQueryChange = onFilterQueryChange,
                    placeholder = stringResource(R.string.folders_filter_hint),
                )
            }

            item {
                Surface(
                    shape = RoundedCornerShape(SnapSpacing.CardRadius),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    border = BorderStroke(1.dp, extended.cardBorder),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = pluralStringResource(
                                R.plurals.folders_status_summary,
                                uiState.totalSlips,
                                uiState.totalSlips,
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        StatusBadge(text = stringResource(R.string.folders_status_offline))
                    }
                }
            }

            item {
                SectionHeader(
                    title = stringResource(R.string.folders_smart_section),
                    trailing = {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ) {
                            Text(
                                text = stringResource(R.string.folders_auto_sorted),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            )
                        }
                    },
                )
            }

            items(
                items = uiState.smartFolders,
                key = { it.category.name },
            ) { stat ->
                SmartFolderCard(
                    stat = stat,
                    onClick = { onFolderClick(stat.category) },
                )
            }

            item {
                SectionHeader(
                    title = stringResource(R.string.folders_my_collections),
                    trailing = { if (!uiState.isPro) ProLockBadge() },
                )
            }

            if (uiState.isPro) {
                items(
                    items = uiState.customCollections,
                    key = { it.id },
                ) { collection ->
                    Surface(
                        shape = RoundedCornerShape(SnapSpacing.CardRadius),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        border = BorderStroke(1.dp, extended.cardBorder),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier.padding(SnapSpacing.md),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(extended.notesContainer),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Folder,
                                    contentDescription = null,
                                    tint = extended.onNotesContainer,
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = collection.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = "${collection.subtitle} • ${collection.count} slips",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            } else {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        uiState.customCollections.take(2).forEach { preview ->
                            Surface(
                                onClick = onProUpsellClick,
                                shape = RoundedCornerShape(SnapSpacing.CardRadius),
                                color = MaterialTheme.colorScheme.surfaceContainerLow,
                                border = BorderStroke(1.dp, extended.cardBorder),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    modifier = Modifier.padding(SnapSpacing.md),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Lock,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(20.dp),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = preview.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                        Text(
                                            text = preview.subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                    ProLockBadge()
                                }
                            }
                        }

                        ProUpsellCard(
                            title = stringResource(R.string.folders_pro_upsell_title),
                            description = stringResource(R.string.folders_pro_upsell_desc),
                            ctaLabel = stringResource(R.string.folders_pro_upsell_cta),
                            onClick = onProUpsellClick,
                        )
                    }
                }
            }

            item {
                InfoTipCard(
                    title = stringResource(R.string.folders_tip_title),
                    body = stringResource(R.string.folders_tip_desc),
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SmartFolderCard(
    stat: CategoryStat,
    onClick: () -> Unit,
) {
    val extended = SnapScribeTheme.extendedColors
    val (catBg, catFg) = extended.categoryColors(stat.category)

    Surface(
        onClick = onClick,
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
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(catBg),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = stat.category.icon,
                        contentDescription = null,
                        tint = catFg,
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = catBg,
                ) {
                    Text(
                        text = pluralStringResource(
                            R.plurals.folders_slips_count,
                            stat.count,
                            stat.count,
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = catFg,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(stat.category.labelRes),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(stat.category.descriptionRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (stat.category == SnapCategory.RECEIPTS) {
                        Text(
                            text = stringResource(R.string.folders_nov_total),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = formatRupiah(stat.totalAmountRupiah),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.folders_latest_label),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stat.latestSummary.ifBlank { "-" },
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            if (stat.topTags.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    stat.topTags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            border = BorderStroke(1.dp, extended.cardBorder),
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FoldersLightPreview() {
    SnapScribeTheme(themeMode = ThemeMode.LIGHT) {
        FoldersScreen(
            uiState = FoldersUiState(
                totalSlips = 20,
                smartFolders = listOf(
                    CategoryStat(SnapCategory.RECEIPTS, 7, 1_448_500L, "Transfer BCA", listOf("BCA", "Superindo")),
                    CategoryStat(SnapCategory.CODES_LINKS, 5, 0L, "Roast & Brew Guest", listOf("Wi-Fi", "JNE Express")),
                ),
            ),
            onFilterQueryChange = {},
            onFolderClick = {},
            onProfileClick = {},
            onAddFolderClick = {},
            onProUpsellClick = {},
        )
    }
}
