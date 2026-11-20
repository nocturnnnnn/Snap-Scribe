package com.genius.snapscribe.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.genius.snapscribe.R
import com.genius.snapscribe.data.sample.SampleSnaps
import com.genius.snapscribe.domain.model.ImageSource
import com.genius.snapscribe.domain.model.QuickAction
import com.genius.snapscribe.domain.model.QuickActionType
import com.genius.snapscribe.domain.model.Snap
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.domain.model.primaryAction
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing
import com.genius.snapscribe.util.formatRelativeTimestamp
import java.time.Instant

@Composable
fun SnapImage(
    image: ImageSource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    when (image) {
        is ImageSource.Drawable -> {
            Image(
                painter = painterResource(id = image.resId),
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = modifier,
            )
        }

        is ImageSource.Uri -> {
            Box(
                modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(32.dp),
                )
            }
        }
    }
}

@Composable
fun SnapCard(
    snap: Snap,
    onClick: () -> Unit,
    onQuickAction: (QuickAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = SnapScribeTheme.extendedColors
    val action = snap.primaryAction()
    val todayLabel = stringResource(R.string.group_today)
    val yesterdayLabel = stringResource(R.string.group_yesterday)
    val dateLabel = snap.capturedAt.formatRelativeTimestamp(
        todayLabel = todayLabel,
        yesterdayLabel = yesterdayLabel,
    )
    val subtitleText = snap.entities.firstOrNull()?.value ?: snap.merchantOrTag

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(SnapSpacing.CardRadius),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, extended.cardBorder),
        shadowElevation = if (extended.isDark) 0.dp else 1.dp,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.35f)
                    .clip(RoundedCornerShape(SnapSpacing.CropRadius)),
            ) {
                SnapImage(
                    image = snap.image,
                    contentDescription = snap.title,
                    modifier = Modifier.matchParentSize(),
                )
                CategoryPill(
                    category = snap.category,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = 2.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = dateLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = snap.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = snap.category.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = subtitleText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            ActionChipButton(
                action = action,
                onClick = { onQuickAction(action) },
                emphasized = action.type == QuickActionType.OPEN_MAPS,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 200)
@Composable
private fun SnapCardLightPreview() {
    val sample = SampleSnaps.buildDefaultSnaps(Instant.parse("2026-11-20T09:00:00Z")).first()
    SnapScribeTheme(themeMode = ThemeMode.LIGHT) {
        SnapCard(
            snap = sample,
            onClick = {},
            onQuickAction = {},
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 200)
@Composable
private fun SnapCardDarkPreview() {
    val sample = SampleSnaps.buildDefaultSnaps(Instant.parse("2026-11-20T09:00:00Z")).first()
    SnapScribeTheme(themeMode = ThemeMode.DARK) {
        SnapCard(
            snap = sample,
            onClick = {},
            onQuickAction = {},
            modifier = Modifier.padding(8.dp),
        )
    }
}
