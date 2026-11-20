package com.genius.snapscribe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.genius.snapscribe.R
import com.genius.snapscribe.domain.model.QuickAction
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing
import kotlinx.coroutines.delay

@Composable
fun ActionChipButton(
    action: QuickAction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    emphasized: Boolean = false,
) {
    val extended = SnapScribeTheme.extendedColors
    var showCopied by remember(action) { mutableStateOf(false) }

    LaunchedEffect(showCopied) {
        if (showCopied) {
            delay(2000L)
            showCopied = false
        }
    }

    val targetContainer = when {
        showCopied -> extended.successContainer
        emphasized -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceContainerLow
    }
    val targetContent = when {
        showCopied -> extended.onSuccess
        emphasized -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurface
    }
    val borderColor = when {
        showCopied -> extended.onSuccess.copy(alpha = 0.35f)
        emphasized -> MaterialTheme.colorScheme.primary
        else -> extended.cardBorder
    }

    val containerColor by animateColorAsState(targetValue = targetContainer, label = "action_bg")
    val contentColor by animateColorAsState(targetValue = targetContent, label = "action_fg")

    OutlinedButton(
        onClick = {
            if (action.type.isClipboardAction) {
                showCopied = true
            }
            onClick()
        },
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
        modifier = modifier
            .defaultMinSize(minHeight = SnapSpacing.MinTouchTarget)
            .semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = if (showCopied) Icons.Outlined.Check else action.type.icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (showCopied) {
                    stringResource(R.string.action_copied)
                } else {
                    stringResource(action.type.labelRes)
                },
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
