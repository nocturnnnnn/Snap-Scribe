package com.genius.snapscribe.ui.screens.home

import android.app.Activity
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.genius.snapscribe.R
import com.genius.snapscribe.data.sample.SampleSnaps
import com.genius.snapscribe.domain.model.QuickAction
import com.genius.snapscribe.domain.model.QuickActionType
import com.genius.snapscribe.domain.model.SnapCategory
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.ui.components.CategoryFilterRow
import com.genius.snapscribe.ui.components.SnapBrandTopBar
import com.genius.snapscribe.ui.components.SnapCard
import com.genius.snapscribe.ui.components.SnapSearchField
import com.genius.snapscribe.ui.components.StatusBadge
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing
import com.genius.snapscribe.util.addCalendarEvent
import com.genius.snapscribe.util.copyToClipboard
import com.genius.snapscribe.util.createVoiceSearchIntent
import com.genius.snapscribe.util.openMaps
import com.genius.snapscribe.util.openUrl
import java.time.Instant

@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    onSnapClick: (String) -> Unit,
    onSearchClick: (String?) -> Unit,
    onProfileClick: () -> Unit,
    onAutoOrganizeClick: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val resources = LocalResources.current

    val voicePrompt = stringResource(R.string.cd_voice_search)
    val voiceUnavailable = stringResource(R.string.snackbar_voice_unavailable)
    val intentUnavailable = stringResource(R.string.snackbar_intent_unavailable)

    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val query = matches?.firstOrNull()?.trim()
            if (!query.isNullOrEmpty()) {
                onSearchClick(query)
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        onSelectCategory = viewModel::selectCategory,
        onSortOrderChange = viewModel::setSortOrder,
        onSnapClick = onSnapClick,
        onSearchBarClick = { onSearchClick(null) },
        onVoiceSearchClick = {
            runCatching {
                voiceLauncher.launch(createVoiceSearchIntent(voicePrompt))
            }.onFailure {
                onShowMessage(voiceUnavailable)
            }
        },
        onQuickAction = { action, snapTitle ->
            when (action.type) {
                QuickActionType.COPY_AMOUNT,
                QuickActionType.COPY_PASSWORD,
                QuickActionType.COPY_RESI,
                QuickActionType.COPY_TEXT -> {
                    copyToClipboard(
                        context = context,
                        label = snapTitle,
                        text = action.payload,
                        isSensitive = action.type.isSensitive,
                    )
                    onShowMessage(resources.getString(R.string.snackbar_copied, action.payload))
                }
                QuickActionType.OPEN_MAPS -> {
                    if (!openMaps(context, action.payload)) {
                        onShowMessage(intentUnavailable)
                    }
                }
                QuickActionType.OPEN_LINK -> {
                    if (!openUrl(context, action.payload)) {
                        onShowMessage(intentUnavailable)
                    }
                }
                QuickActionType.ADD_CALENDAR -> {
                    if (!addCalendarEvent(context, snapTitle, action.payload)) {
                        onShowMessage(intentUnavailable)
                    }
                }
            }
        },
        onProfileClick = onProfileClick,
        onAutoOrganizeClick = onAutoOrganizeClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSelectCategory: (SnapCategory?) -> Unit,
    onSortOrderChange: (SnapSortOrder) -> Unit,
    onSnapClick: (String) -> Unit,
    onSearchBarClick: () -> Unit,
    onVoiceSearchClick: () -> Unit,
    onQuickAction: (QuickAction, String) -> Unit,
    onProfileClick: () -> Unit,
    onAutoOrganizeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = SnapScribeTheme.extendedColors
    var showSortSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        SnapBrandTopBar(
            subtitle = stringResource(R.string.nav_snaps),
            userInitials = uiState.userInitials,
            isSignedIn = uiState.isSignedIn,
            onProfileClick = onProfileClick,
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(
                start = SnapSpacing.md,
                end = SnapSpacing.md,
                top = SnapSpacing.xs,
                bottom = SnapSpacing.BottomNavClearance,
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.home_title),
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.semantics { heading() },
                            )
                            StatusBadge(
                                text = pluralStringResource(
                                    R.plurals.home_status_ready,
                                    uiState.totalCount,
                                    uiState.totalCount,
                                ),
                            )
                        }

                        val sortCd = stringResource(R.string.cd_sort_filter)
                        Surface(
                            onClick = { showSortSheet = true },
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceContainerLowest,
                            border = BorderStroke(1.dp, extended.cardBorder),
                            modifier = Modifier
                                .size(SnapSpacing.MinTouchTarget)
                                .semantics { contentDescription = sortCd },
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Tune,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }

                    SnapSearchField(
                        query = "",
                        onQueryChange = {},
                        placeholder = stringResource(R.string.home_search_placeholder),
                        onClickOverride = onSearchBarClick,
                        onVoiceSearchClick = onVoiceSearchClick,
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                // Full-bleed horizontal filter row inside padded grid
                Box(modifier = Modifier.padding(vertical = 2.dp)) {
                    CategoryFilterRow(
                        selectedCategory = uiState.selectedCategory,
                        totalCount = uiState.totalCount,
                        counts = uiState.categoryCounts,
                        onSelectCategory = onSelectCategory,
                    )
                }
            }

            if (uiState.snaps.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HomeEmptyState(
                        modifier = Modifier.padding(vertical = 32.dp),
                    )
                }
            } else {
                items(
                    items = uiState.snaps,
                    key = { it.id },
                ) { snap ->
                    SnapCard(
                        snap = snap,
                        onClick = { onSnapClick(snap.id) },
                        onQuickAction = { action -> onQuickAction(action, snap.title) },
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Surface(
                    onClick = onAutoOrganizeClick,
                    shape = RoundedCornerShape(SnapSpacing.CardRadius),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    border = BorderStroke(1.dp, extended.cardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(SnapSpacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(extended.codesContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PhotoCamera,
                                contentDescription = null,
                                tint = extended.onCodesContainer,
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.home_banner_title),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = stringResource(R.string.home_banner_desc),
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
        }
    }

    if (showSortSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showSortSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SnapSpacing.md, vertical = SnapSpacing.sm)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(R.string.home_sort_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
                SortOptionRow(
                    label = stringResource(R.string.home_sort_newest),
                    selected = uiState.sortOrder == SnapSortOrder.NEWEST,
                    onClick = {
                        onSortOrderChange(SnapSortOrder.NEWEST)
                        showSortSheet = false
                    },
                )
                SortOptionRow(
                    label = stringResource(R.string.home_sort_oldest),
                    selected = uiState.sortOrder == SnapSortOrder.OLDEST,
                    onClick = {
                        onSortOrderChange(SnapSortOrder.OLDEST)
                        showSortSheet = false
                    },
                )
            }
        }
    }
}

@Composable
private fun SortOptionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        },
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = SnapSpacing.MinTouchTarget)
            .clip(RoundedCornerShape(14.dp))
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(selected = selected, onClick = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun HomeEmptyState(modifier: Modifier = Modifier) {
    val extended = SnapScribeTheme.extendedColors
    Surface(
        shape = RoundedCornerShape(SnapSpacing.CardRadius),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, extended.cardBorder),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ReceiptLong,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(28.dp),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.home_empty_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.home_empty_desc),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeLightPreview() {
    val sampleSnaps = SampleSnaps.buildDefaultSnaps(Instant.parse("2026-11-20T09:00:00Z")).take(4)
    SnapScribeTheme(themeMode = ThemeMode.LIGHT) {
        HomeScreen(
            uiState = HomeUiState(
                snaps = sampleSnaps,
                totalCount = 20,
                categoryCounts = mapOf(
                    SnapCategory.RECEIPTS to 7,
                    SnapCategory.CODES_LINKS to 5,
                    SnapCategory.ADDRESSES to 4,
                    SnapCategory.NOTES to 4,
                ),
            ),
            onSelectCategory = {},
            onSortOrderChange = {},
            onSnapClick = {},
            onSearchBarClick = {},
            onVoiceSearchClick = {},
            onQuickAction = { _, _ -> },
            onProfileClick = {},
            onAutoOrganizeClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeDarkPreview() {
    val sampleSnaps = SampleSnaps.buildDefaultSnaps(Instant.parse("2026-11-20T09:00:00Z")).take(4)
    SnapScribeTheme(themeMode = ThemeMode.DARK) {
        HomeScreen(
            uiState = HomeUiState(
                snaps = sampleSnaps,
                totalCount = 20,
                isSignedIn = true,
                userInitials = "BS",
            ),
            onSelectCategory = {},
            onSortOrderChange = {},
            onSnapClick = {},
            onSearchBarClick = {},
            onVoiceSearchClick = {},
            onQuickAction = { _, _ -> },
            onProfileClick = {},
            onAutoOrganizeClick = {},
        )
    }
}
