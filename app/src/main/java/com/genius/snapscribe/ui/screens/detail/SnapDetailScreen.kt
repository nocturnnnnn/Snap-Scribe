package com.genius.snapscribe.ui.screens.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.genius.snapscribe.R
import com.genius.snapscribe.data.sample.SampleSnaps
import com.genius.snapscribe.domain.model.QuickAction
import com.genius.snapscribe.domain.model.QuickActionType
import com.genius.snapscribe.domain.model.Snap
import com.genius.snapscribe.domain.model.SnapCategory
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.domain.model.primaryAction
import com.genius.snapscribe.ui.components.ActionChipButton
import com.genius.snapscribe.ui.components.CategoryPill
import com.genius.snapscribe.ui.components.SegmentedSelector
import com.genius.snapscribe.ui.components.SnapBackTopBar
import com.genius.snapscribe.ui.components.SnapImage
import com.genius.snapscribe.ui.components.SnapSecondaryButton
import com.genius.snapscribe.ui.components.SnapTonalButton
import com.genius.snapscribe.ui.components.StatusBadge
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing
import com.genius.snapscribe.util.addCalendarEvent
import com.genius.snapscribe.util.copyToClipboard
import com.genius.snapscribe.util.formatRelativeTimestamp
import com.genius.snapscribe.util.openMaps
import com.genius.snapscribe.util.openUrl
import com.genius.snapscribe.util.shareText
import java.time.Instant

enum class DetailViewMode {
    ORIGINAL,
    HIGHLIGHT,
}

@Composable
fun SnapDetailRoute(
    viewModel: SnapDetailViewModel,
    onBack: () -> Unit,
    onZoomClick: (String) -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val resources = LocalResources.current
    val intentUnavailable = stringResource(R.string.snackbar_intent_unavailable)

    val snap = uiState.snap
    if (snap == null) {
        if (uiState.isLoaded) {
            onBack()
        }
        return
    }

    SnapDetailScreen(
        snap = snap,
        onBack = onBack,
        onZoomClick = { onZoomClick(snap.id) },
        onSelectCategory = viewModel::updateCategory,
        onDeleteConfirm = { viewModel.deleteSnap(onDeleted = onBack) },
        onShareAllText = {
            val payload = buildString {
                appendLine(snap.title)
                appendLine()
                append(snap.fullText.joinToString("\n"))
            }
            if (!shareText(context, snap.title, payload)) {
                onShowMessage(intentUnavailable)
            }
        },
        onCopyAllText = {
            val allText = snap.fullText.joinToString("\n")
            copyToClipboard(context, snap.title, allText)
            onShowMessage(resources.getString(R.string.snackbar_copied, snap.title))
        },
        onQuickAction = { action ->
            when (action.type) {
                QuickActionType.COPY_AMOUNT,
                QuickActionType.COPY_PASSWORD,
                QuickActionType.COPY_RESI,
                QuickActionType.COPY_TEXT -> {
                    copyToClipboard(
                        context = context,
                        label = snap.title,
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
                    if (!addCalendarEvent(context, snap.title, action.payload)) {
                        onShowMessage(intentUnavailable)
                    }
                }
            }
        },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SnapDetailScreen(
    snap: Snap,
    onBack: () -> Unit,
    onZoomClick: () -> Unit,
    onSelectCategory: (SnapCategory) -> Unit,
    onDeleteConfirm: () -> Unit,
    onShareAllText: () -> Unit,
    onCopyAllText: () -> Unit,
    onQuickAction: (QuickAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = SnapScribeTheme.extendedColors
    var viewMode by remember { mutableStateOf(DetailViewMode.HIGHLIGHT) }
    var showCategorySheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val todayLabel = stringResource(R.string.group_today)
    val yesterdayLabel = stringResource(R.string.group_yesterday)
    val dateLabel = snap.capturedAt.formatRelativeTimestamp(todayLabel, yesterdayLabel)
    val primaryAction = snap.primaryAction()
    val highlightValue = snap.entities.firstOrNull()?.value ?: snap.title

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
    ) {
        SnapBackTopBar(
            title = stringResource(R.string.detail_title),
            onBack = onBack,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = SnapSpacing.md, vertical = SnapSpacing.xs)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Date + Share + Delete strip
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    border = BorderStroke(1.dp, extended.cardBorder),
                    modifier = Modifier.defaultMinSize(minHeight = SnapSpacing.MinTouchTarget),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = dateLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                SnapTonalButton(
                    text = stringResource(R.string.action_share),
                    leadingIcon = Icons.Outlined.Share,
                    onClick = onShareAllText,
                )

                SnapTonalButton(
                    text = stringResource(R.string.action_delete),
                    leadingIcon = Icons.Outlined.DeleteOutline,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    onClick = { showDeleteDialog = true },
                )
            }

            // Original / Highlight Text segmented toggle
            SegmentedSelector(
                options = DetailViewMode.entries,
                selectedOption = viewMode,
                labelProvider = { mode ->
                    when (mode) {
                        DetailViewMode.ORIGINAL -> stringResource(R.string.detail_mode_original)
                        DetailViewMode.HIGHLIGHT -> stringResource(R.string.detail_mode_highlight)
                    }
                },
                onSelect = { viewMode = it },
            )

            // Preview card with optional OCR Bounding Box Highlight overlay & Zoom trigger
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                border = BorderStroke(1.dp, extended.cardBorder),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .aspectRatio(1.28f)
                        .clip(RoundedCornerShape(14.dp)),
                ) {
                    SnapImage(
                        image = snap.image,
                        contentDescription = snap.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize(),
                    )

                    if (viewMode == DetailViewMode.HIGHLIGHT) {
                        val fillColor = extended.ocrHighlightFill
                        val strokeColor = extended.ocrHighlightBorder
                        Canvas(modifier = Modifier.matchParentSize()) {
                            val radius = 8.dp.toPx()
                            val strokeWidth = 2.dp.toPx()
                            snap.blocks.forEach { block ->
                                val left = block.box.left * size.width
                                val top = block.box.top * size.height
                                val rectWidth = (block.box.right - block.box.left) * size.width
                                val rectHeight = (block.box.bottom - block.box.top) * size.height
                                if (rectWidth > 0f && rectHeight > 0f) {
                                    drawRoundRect(
                                        color = fillColor,
                                        topLeft = Offset(left, top),
                                        size = Size(rectWidth, rectHeight),
                                        cornerRadius = CornerRadius(radius, radius),
                                    )
                                    drawRoundRect(
                                        color = strokeColor,
                                        topLeft = Offset(left, top),
                                        size = Size(rectWidth, rectHeight),
                                        cornerRadius = CornerRadius(radius, radius),
                                        style = Stroke(width = strokeWidth),
                                    )
                                }
                            }
                        }
                    }

                    val zoomCd = stringResource(R.string.cd_zoom_image)
                    Surface(
                        onClick = onZoomClick,
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.85f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                            .size(SnapSpacing.MinTouchTarget)
                            .semantics { contentDescription = zoomCd },
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.ZoomIn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.inverseOnSurface,
                            )
                        }
                    }
                }
            }

            // Smart Detection Ledger Card
            Surface(
                shape = RoundedCornerShape(20.dp),
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.detail_smart_detection),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.semantics { heading() },
                        )
                        StatusBadge(text = stringResource(R.string.detail_verified_badge))
                    }

                    CategoryPill(
                        category = snap.category,
                        editable = true,
                        onClick = { showCategorySheet = true },
                    )

                    Text(
                        text = highlightValue,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = snap.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    ActionChipButton(
                        action = primaryAction,
                        onClick = { onQuickAction(primaryAction) },
                        emphasized = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            // All Extracted Text section
            Surface(
                shape = RoundedCornerShape(20.dp),
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.detail_all_extracted),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .weight(1f)
                                .semantics { heading() },
                        )
                        TextButton(
                            onClick = onCopyAllText,
                            modifier = Modifier.defaultMinSize(minHeight = SnapSpacing.MinTouchTarget),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.detail_copy_all),
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        border = BorderStroke(1.dp, extended.cardBorder),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            snap.fullText.forEachIndexed { idx, line ->
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Text(
                                        text = (idx + 1).toString().padStart(2, '0'),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Text(
                                        text = line,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                            }
                        }
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        SnapSecondaryButton(
                            text = stringResource(R.string.detail_share_text),
                            leadingIcon = Icons.Outlined.Share,
                            onClick = onShareAllText,
                            modifier = Modifier.weight(1f, fill = false),
                        )
                        SnapSecondaryButton(
                            text = stringResource(R.string.detail_change_category),
                            leadingIcon = Icons.Outlined.Edit,
                            onClick = { showCategorySheet = true },
                            modifier = Modifier.weight(1f, fill = false),
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.detail_footer_saved),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }

    if (showCategorySheet) {
        CategoryPickerSheet(
            currentCategory = snap.category,
            onSelectCategory = { newCategory ->
                onSelectCategory(newCategory)
                showCategorySheet = false
            },
            onDismiss = { showCategorySheet = false },
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.detail_delete_title)) },
            text = { Text(stringResource(R.string.detail_delete_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteConfirm()
                    },
                ) {
                    Text(
                        text = stringResource(R.string.action_delete),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SnapDetailLightPreview() {
    val sample = SampleSnaps.buildDefaultSnaps(Instant.parse("2026-11-20T09:00:00Z")).first()
    SnapScribeTheme(themeMode = ThemeMode.LIGHT) {
        SnapDetailScreen(
            snap = sample,
            onBack = {},
            onZoomClick = {},
            onSelectCategory = {},
            onDeleteConfirm = {},
            onShareAllText = {},
            onCopyAllText = {},
            onQuickAction = {},
        )
    }
}
