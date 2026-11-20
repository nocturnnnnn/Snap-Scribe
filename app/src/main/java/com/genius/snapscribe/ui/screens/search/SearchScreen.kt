package com.genius.snapscribe.ui.screens.search

import android.app.Activity
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.genius.snapscribe.R
import com.genius.snapscribe.data.repository.SearchHit
import com.genius.snapscribe.data.sample.SampleSnaps
import com.genius.snapscribe.domain.model.QuickAction
import com.genius.snapscribe.domain.model.QuickActionType
import com.genius.snapscribe.domain.model.SnapCategory
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.domain.model.primaryAction
import com.genius.snapscribe.ui.components.ActionChipButton
import com.genius.snapscribe.ui.components.CategoryFilterRow
import com.genius.snapscribe.ui.components.FilterPillChip
import com.genius.snapscribe.ui.components.InfoTipCard
import com.genius.snapscribe.ui.components.SectionHeader
import com.genius.snapscribe.ui.components.SnapBrandTopBar
import com.genius.snapscribe.ui.components.SnapSearchField
import com.genius.snapscribe.ui.components.StatusBadge
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing
import com.genius.snapscribe.util.addCalendarEvent
import com.genius.snapscribe.util.copyToClipboard
import com.genius.snapscribe.util.createVoiceSearchIntent
import com.genius.snapscribe.util.formatRelativeTimestamp
import com.genius.snapscribe.util.formatRupiah
import com.genius.snapscribe.util.openMaps
import com.genius.snapscribe.util.openUrl
import java.time.Instant

@Composable
fun SearchRoute(
    viewModel: SearchViewModel,
    initialQuery: String?,
    onSnapClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val resources = LocalResources.current

    LaunchedEffect(initialQuery) {
        viewModel.applyExternalQuery(initialQuery)
    }

    val voicePrompt = stringResource(R.string.cd_voice_search)
    val voiceUnavailable = stringResource(R.string.snackbar_voice_unavailable)
    val intentUnavailable = stringResource(R.string.snackbar_intent_unavailable)

    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spoken = matches?.firstOrNull()?.trim()
            if (!spoken.isNullOrEmpty()) {
                viewModel.applyExternalQuery(spoken)
            }
        }
    }

    SearchScreen(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        onSearchSubmit = viewModel::onSearchSubmit,
        onSelectRecentSearch = viewModel::onSelectRecentSearch,
        onClearHistory = viewModel::onClearHistory,
        onSelectCategory = viewModel::onSelectCategory,
        onVoiceSearchClick = {
            runCatching {
                voiceLauncher.launch(createVoiceSearchIntent(voicePrompt))
            }.onFailure {
                onShowMessage(voiceUnavailable)
            }
        },
        onSnapClick = onSnapClick,
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
                    if (!openMaps(context, action.payload)) onShowMessage(intentUnavailable)
                }
                QuickActionType.OPEN_LINK -> {
                    if (!openUrl(context, action.payload)) onShowMessage(intentUnavailable)
                }
                QuickActionType.ADD_CALENDAR -> {
                    if (!addCalendarEvent(context, snapTitle, action.payload)) onShowMessage(intentUnavailable)
                }
            }
        },
        onProfileClick = onProfileClick,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    onSelectRecentSearch: (String) -> Unit,
    onClearHistory: () -> Unit,
    onSelectCategory: (SnapCategory?) -> Unit,
    onVoiceSearchClick: () -> Unit,
    onSnapClick: (String) -> Unit,
    onQuickAction: (QuickAction, String) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = SnapScribeTheme.extendedColors

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        SnapBrandTopBar(
            subtitle = stringResource(R.string.nav_search),
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
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SnapSearchField(
                        query = uiState.query,
                        onQueryChange = onQueryChange,
                        placeholder = stringResource(R.string.search_placeholder),
                        onSearchSubmit = onSearchSubmit,
                        onVoiceSearchClick = onVoiceSearchClick,
                    )
                    StatusBadge(
                        text = stringResource(R.string.search_privacy_badge),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            item {
                CategoryFilterRow(
                    selectedCategory = uiState.selectedCategory,
                    totalCount = uiState.results.size,
                    counts = emptyMap(),
                    onSelectCategory = onSelectCategory,
                    showCounts = false,
                )
            }

            if (uiState.recentSearches.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SectionHeader(
                            title = stringResource(R.string.search_recent_title),
                            actionLabel = stringResource(R.string.search_clear_history),
                            onActionClick = onClearHistory,
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            uiState.recentSearches.forEach { term ->
                                val isActive = term.equals(uiState.query.trim(), ignoreCase = true)
                                FilterPillChip(
                                    label = term,
                                    selected = isActive,
                                    icon = if (isActive) Icons.Outlined.Search else Icons.Outlined.History,
                                    onClick = { onSelectRecentSearch(term) },
                                )
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.search_results_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .weight(1f)
                            .semantics { heading() },
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(
                        text = pluralStringResource(
                            R.plurals.search_found_count,
                            uiState.results.size,
                            uiState.results.size,
                        ),
                    )
                }
            }

            if (uiState.results.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(SnapSpacing.CardRadius),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        border = BorderStroke(1.dp, extended.cardBorder),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.search_empty_title, uiState.query),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = stringResource(R.string.search_empty_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            } else {
                items(
                    items = uiState.results,
                    key = { it.snap.id },
                ) { hit ->
                    SearchResultCard(
                        hit = hit,
                        onClick = { onSnapClick(hit.snap.id) },
                        onQuickAction = { action -> onQuickAction(action, hit.snap.title) },
                    )
                }
            }

            item {
                InfoTipCard(
                    title = stringResource(R.string.search_tips_title),
                    body = stringResource(R.string.search_tips_desc),
                )
            }
        }
    }
}

@Composable
private fun SearchResultCard(
    hit: SearchHit,
    onClick: () -> Unit,
    onQuickAction: (QuickAction) -> Unit,
) {
    val extended = SnapScribeTheme.extendedColors
    val snap = hit.snap
    val (catBg, catFg) = extended.categoryColors(snap.category)
    val todayLabel = stringResource(R.string.group_today)
    val yesterdayLabel = stringResource(R.string.group_yesterday)
    val dateText = snap.capturedAt.formatRelativeTimestamp(todayLabel, yesterdayLabel)
    val action = snap.primaryAction()

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(SnapSpacing.CardRadius),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, extended.cardBorder),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(SnapSpacing.md),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(catBg),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = snap.category.icon,
                        contentDescription = null,
                        tint = catFg,
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = snap.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "$dateText • ${snap.merchantOrTag}",
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

            // OCR Snippet box with highlighted search matches
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                border = BorderStroke(1.dp, extended.cardBorder),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DocumentScanner,
                        contentDescription = stringResource(R.string.search_ocr_detected_label),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = buildHighlightedSnippet(
                            text = hit.matchedSnippet,
                            ranges = hit.matchRanges,
                            highlightBg = extended.ocrHighlightFill,
                            highlightFg = MaterialTheme.colorScheme.onSurface,
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Verified,
                        contentDescription = null,
                        tint = extended.onSuccess,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = stringResource(snap.category.labelRes),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                ActionChipButton(
                    action = action,
                    onClick = { onQuickAction(action) },
                    modifier = Modifier.defaultMinSize(minHeight = SnapSpacing.MinTouchTarget),
                )
            }
        }
    }
}

private fun buildHighlightedSnippet(
    text: String,
    ranges: List<IntRange>,
    highlightBg: androidx.compose.ui.graphics.Color,
    highlightFg: androidx.compose.ui.graphics.Color,
): AnnotatedString = buildAnnotatedString {
    append(text)
    ranges.forEach { range ->
        val safeStart = range.first.coerceIn(0, text.length)
        val safeEnd = (range.last + 1).coerceIn(safeStart, text.length)
        if (safeStart < safeEnd) {
            addStyle(
                style = SpanStyle(
                    background = highlightBg,
                    color = highlightFg,
                    fontWeight = FontWeight.Bold,
                ),
                start = safeStart,
                end = safeEnd,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchLightPreview() {
    val samples = SampleSnaps.buildDefaultSnaps(Instant.parse("2026-11-20T09:00:00Z"))
    val hits = samples.take(3).map { snap ->
        val snippet = snap.fullText.joinToString(" • ")
        SearchHit(snap = snap, matchedSnippet = snippet, matchRanges = listOf(0..9))
    }
    SnapScribeTheme(themeMode = ThemeMode.LIGHT) {
        SearchScreen(
            uiState = SearchUiState(
                query = "Superindo",
                recentSearches = listOf("Wi-Fi", "Superindo", "Token PLN"),
                results = hits,
            ),
            onQueryChange = {},
            onSearchSubmit = {},
            onSelectRecentSearch = {},
            onClearHistory = {},
            onSelectCategory = {},
            onVoiceSearchClick = {},
            onSnapClick = {},
            onQuickAction = { _, _ -> },
            onProfileClick = {},
        )
    }
}
