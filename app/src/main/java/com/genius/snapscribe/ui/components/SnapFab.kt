package com.genius.snapscribe.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.genius.snapscribe.R
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing

@Composable
fun SnapFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 4.dp,
            pressedElevation = 6.dp,
        ),
        modifier = modifier.defaultMinSize(minHeight = SnapSpacing.ButtonHeight),
        icon = {
            Icon(
                imageVector = Icons.Outlined.PhotoCamera,
                contentDescription = null,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.fab_snap_photo),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
        },
    )
}

@Preview
@Composable
private fun SnapFabPreview() {
    SnapScribeTheme(themeMode = ThemeMode.DARK) {
        SnapFab(onClick = {})
    }
}
