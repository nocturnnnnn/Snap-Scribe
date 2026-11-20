package com.genius.snapscribe.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.genius.snapscribe.R
import com.genius.snapscribe.data.local.SettingsRepository
import com.genius.snapscribe.domain.model.AppSettings
import com.genius.snapscribe.domain.model.SnapSource
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.ui.components.SettingsCardSection
import com.genius.snapscribe.ui.components.SnapBackTopBar
import com.genius.snapscribe.ui.components.SnapPrimaryButton
import com.genius.snapscribe.ui.components.StatusBadge
import com.genius.snapscribe.ui.components.ToggleRow
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing
import kotlinx.coroutines.launch

@Composable
fun IndexSetupRoute(
    settingsRepository: SettingsRepository,
    onBack: () -> Unit,
    onCompleteSetup: () -> Unit,
) {
    val settings by settingsRepository.settings.collectAsStateWithLifecycle(
        initialValue = AppSettings(),
    )
    val scope = rememberCoroutineScope()

    IndexSetupScreen(
        indexedFolders = settings.indexedFolders,
        onBack = onBack,
        onToggleFolder = { source, enabled ->
            scope.launch { settingsRepository.toggleIndexedFolder(source, enabled) }
        },
        onAllowAndStart = {
            scope.launch {
                settingsRepository.setOnboardingDone(true)
                onCompleteSetup()
            }
        },
    )
}

@Composable
fun IndexSetupScreen(
    indexedFolders: Set<SnapSource>,
    onBack: () -> Unit,
    onToggleFolder: (SnapSource, Boolean) -> Unit,
    onAllowAndStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
    ) {
        SnapBackTopBar(
            title = stringResource(R.string.app_name),
            onBack = onBack,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = SnapSpacing.md, vertical = SnapSpacing.md),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            StatusBadge(text = stringResource(R.string.setup_privacy_badge))

            Text(
                text = stringResource(R.string.setup_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                text = stringResource(R.string.setup_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            SettingsCardSection(
                title = stringResource(R.string.settings_albums_title),
                subtitle = stringResource(R.string.settings_albums_desc),
                icon = Icons.Outlined.FolderOpen,
            ) {
                ToggleRow(
                    title = stringResource(R.string.album_screenshots_title),
                    description = stringResource(R.string.album_screenshots_desc),
                    checked = SnapSource.SCREENSHOTS in indexedFolders,
                    onCheckedChange = { onToggleFolder(SnapSource.SCREENSHOTS, it) },
                    icon = Icons.Outlined.Smartphone,
                )
                ToggleRow(
                    title = stringResource(R.string.album_whatsapp_title),
                    description = stringResource(R.string.album_whatsapp_desc),
                    checked = SnapSource.WHATSAPP in indexedFolders,
                    onCheckedChange = { onToggleFolder(SnapSource.WHATSAPP, it) },
                    icon = Icons.Outlined.ChatBubbleOutline,
                )
                ToggleRow(
                    title = stringResource(R.string.album_camera_title),
                    description = stringResource(R.string.album_camera_desc),
                    checked = SnapSource.CAMERA in indexedFolders,
                    onCheckedChange = { onToggleFolder(SnapSource.CAMERA, it) },
                    icon = Icons.Outlined.PhotoCamera,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.setup_footer_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            SnapPrimaryButton(
                text = stringResource(R.string.setup_cta),
                trailingIcon = Icons.AutoMirrored.Rounded.ArrowForward,
                onClick = onAllowAndStart,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IndexSetupPreview() {
    SnapScribeTheme(themeMode = ThemeMode.LIGHT) {
        IndexSetupScreen(
            indexedFolders = setOf(SnapSource.SCREENSHOTS, SnapSource.WHATSAPP),
            onBack = {},
            onToggleFolder = { _, _ -> },
            onAllowAndStart = {},
        )
    }
}
