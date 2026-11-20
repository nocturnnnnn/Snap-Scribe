package com.genius.snapscribe.ui.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Storage
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.genius.snapscribe.R
import com.genius.snapscribe.domain.model.AppLanguage
import com.genius.snapscribe.domain.model.AppSettings
import com.genius.snapscribe.domain.model.BillingCycle
import com.genius.snapscribe.domain.model.SnapSource
import com.genius.snapscribe.domain.model.TextScale
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.ui.components.ProUpsellCard
import com.genius.snapscribe.ui.components.SegmentedSelector
import com.genius.snapscribe.ui.components.SettingsCardSection
import com.genius.snapscribe.ui.components.SnapBrandTopBar
import com.genius.snapscribe.ui.components.SnapSecondaryButton
import com.genius.snapscribe.ui.components.StatusBadge
import com.genius.snapscribe.ui.components.ThemeOptionCard
import com.genius.snapscribe.ui.components.ToggleRow
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel,
    onProfileClick: () -> Unit,
    onProClick: (String) -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cacheClearedMsg = stringResource(R.string.settings_cache_cleared)

    var selectedLanguage by remember {
        mutableStateOf(readCurrentAppLanguage())
    }

    SettingsScreen(
        uiState = uiState,
        selectedLanguage = selectedLanguage,
        onSelectTextScale = viewModel::setTextScale,
        onToggleHighContrast = viewModel::setHighContrast,
        onSelectLanguage = { language ->
            selectedLanguage = language
            val locales = if (language.tag == null) {
                LocaleListCompat.getEmptyLocaleList()
            } else {
                LocaleListCompat.forLanguageTags(language.tag)
            }
            AppCompatDelegate.setApplicationLocales(locales)
        },
        onToggleIndexedFolder = viewModel::toggleIndexedFolder,
        onSelectThemeMode = viewModel::setThemeMode,
        onProClick = { onProClick("settings") },
        onClearCache = {
            viewModel.clearCache()
            onShowMessage(cacheClearedMsg)
        },
        onProfileClick = onProfileClick,
    )
}

private fun readCurrentAppLanguage(): AppLanguage {
    val locales = AppCompatDelegate.getApplicationLocales()
    val tag = if (locales.isEmpty) null else locales[0]?.language
    return when (tag) {
        "id", "in" -> AppLanguage.INDONESIAN
        "en" -> AppLanguage.ENGLISH
        else -> AppLanguage.SYSTEM
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    selectedLanguage: AppLanguage,
    onSelectTextScale: (TextScale) -> Unit,
    onToggleHighContrast: (Boolean) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleIndexedFolder: (SnapSource, Boolean) -> Unit,
    onSelectThemeMode: (ThemeMode) -> Unit,
    onProClick: () -> Unit,
    onClearCache: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = SnapScribeTheme.extendedColors
    val settings = uiState.settings
    var showClearCacheDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        SnapBrandTopBar(
            subtitle = stringResource(R.string.nav_settings),
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
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusBadge(text = stringResource(R.string.settings_badge))
                    Text(
                        text = stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.semantics { heading() },
                    )
                    Text(
                        text = stringResource(R.string.settings_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // 1. Reading Comfort & Live Preview
            item {
                SettingsCardSection(
                    title = stringResource(R.string.settings_reading_title),
                    subtitle = stringResource(R.string.settings_reading_desc),
                    icon = Icons.Outlined.FormatSize,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.settings_scale_label),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "A  A  A",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    SegmentedSelector(
                        options = TextScale.entries,
                        selectedOption = settings.textScale,
                        labelProvider = { scale ->
                            when (scale) {
                                TextScale.STANDARD -> stringResource(R.string.settings_scale_standard)
                                TextScale.LARGE -> stringResource(R.string.settings_scale_large)
                                TextScale.EXTRA_LARGE -> stringResource(R.string.settings_scale_xl)
                            }
                        },
                        onSelect = onSelectTextScale,
                    )

                    // Live preview card reflecting active theme/scale/contrast
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        border = BorderStroke(1.dp, extended.cardBorder),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.settings_preview_label),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = stringResource(R.string.settings_preview_sample_title),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = stringResource(R.string.settings_preview_sample_body),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                    ToggleRow(
                        title = stringResource(R.string.settings_high_contrast_title),
                        description = stringResource(R.string.settings_high_contrast_desc),
                        checked = settings.highContrast,
                        onCheckedChange = onToggleHighContrast,
                    )
                }
            }

            // 2. App Language
            item {
                SettingsCardSection(
                    title = stringResource(R.string.settings_language_title),
                    subtitle = stringResource(R.string.settings_language_desc),
                    icon = Icons.Outlined.Language,
                    iconContainerColor = extended.notesContainer,
                    iconTint = extended.onNotesContainer,
                ) {
                    SegmentedSelector(
                        options = AppLanguage.entries,
                        selectedOption = selectedLanguage,
                        labelProvider = { lang ->
                            when (lang) {
                                AppLanguage.SYSTEM -> stringResource(R.string.settings_lang_system)
                                AppLanguage.INDONESIAN -> stringResource(R.string.settings_lang_id)
                                AppLanguage.ENGLISH -> stringResource(R.string.settings_lang_en)
                            }
                        },
                        onSelect = onSelectLanguage,
                    )
                }
            }

            // 3. Albums to Index
            item {
                SettingsCardSection(
                    title = stringResource(R.string.settings_albums_title),
                    subtitle = stringResource(R.string.settings_albums_desc),
                    icon = Icons.Outlined.FolderOpen,
                    iconContainerColor = extended.addressesContainer,
                    iconTint = extended.onAddressesContainer,
                ) {
                    ToggleRow(
                        title = stringResource(R.string.album_screenshots_title),
                        description = stringResource(R.string.album_screenshots_desc),
                        checked = SnapSource.SCREENSHOTS in settings.indexedFolders,
                        onCheckedChange = { onToggleIndexedFolder(SnapSource.SCREENSHOTS, it) },
                        icon = Icons.Outlined.Smartphone,
                    )
                    ToggleRow(
                        title = stringResource(R.string.album_whatsapp_title),
                        description = stringResource(R.string.album_whatsapp_desc),
                        checked = SnapSource.WHATSAPP in settings.indexedFolders,
                        onCheckedChange = { onToggleIndexedFolder(SnapSource.WHATSAPP, it) },
                        icon = Icons.Outlined.ChatBubbleOutline,
                    )
                    ToggleRow(
                        title = stringResource(R.string.album_camera_title),
                        description = stringResource(R.string.album_camera_desc),
                        checked = SnapSource.CAMERA in settings.indexedFolders,
                        onCheckedChange = { onToggleIndexedFolder(SnapSource.CAMERA, it) },
                        icon = Icons.Outlined.PhotoCamera,
                    )
                }
            }

            // 4. Notebook Theme
            item {
                SettingsCardSection(
                    title = stringResource(R.string.settings_theme_title),
                    subtitle = stringResource(R.string.settings_theme_desc),
                    icon = Icons.Outlined.Palette,
                    iconContainerColor = extended.receiptsContainer,
                    iconTint = extended.onReceiptsContainer,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ThemeOptionCard(
                            title = stringResource(R.string.settings_theme_light),
                            subtitle = stringResource(R.string.settings_theme_light_sub),
                            selected = settings.themeMode == ThemeMode.LIGHT,
                            previewBackground = Color(0xFFFFF8F4),
                            previewLinePrimary = Color(0xFFA43716),
                            previewLineSecondary = Color(0xFFDFC0B7),
                            onClick = { onSelectThemeMode(ThemeMode.LIGHT) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                        ThemeOptionCard(
                            title = stringResource(R.string.settings_theme_dark),
                            subtitle = stringResource(R.string.settings_theme_dark_sub),
                            selected = settings.themeMode == ThemeMode.DARK,
                            previewBackground = Color(0xFF261F1B),
                            previewLinePrimary = Color(0xFFE89A3C),
                            previewLineSecondary = Color(0xFF524436),
                            onClick = { onSelectThemeMode(ThemeMode.DARK) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                        ThemeOptionCard(
                            title = stringResource(R.string.settings_theme_system),
                            subtitle = stringResource(R.string.settings_theme_system_sub),
                            selected = settings.themeMode == ThemeMode.SYSTEM,
                            previewBackground = MaterialTheme.colorScheme.surfaceContainerHigh,
                            previewLinePrimary = MaterialTheme.colorScheme.onSurface,
                            previewLineSecondary = MaterialTheme.colorScheme.outline,
                            badgeIcon = Icons.Outlined.BrightnessAuto,
                            onClick = { onSelectThemeMode(ThemeMode.SYSTEM) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    }
                }
            }

            // 5. SnapScribe Pro
            item {
                if (uiState.isPro) {
                    val cycleText = when (uiState.proCycle) {
                        BillingCycle.MONTHLY -> stringResource(R.string.pro_billing_monthly)
                        BillingCycle.YEARLY, null -> stringResource(R.string.pro_billing_yearly)
                    }
                    SettingsCardSection(
                        title = stringResource(R.string.settings_pro_title),
                        subtitle = stringResource(R.string.settings_pro_desc_active, cycleText),
                        icon = Icons.Outlined.AutoAwesome,
                        iconContainerColor = extended.receiptsContainer,
                        iconTint = extended.onReceiptsContainer,
                    ) {
                        SnapSecondaryButton(
                            text = stringResource(R.string.settings_pro_cta_active),
                            onClick = onProClick,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                } else {
                    ProUpsellCard(
                        title = stringResource(R.string.settings_pro_title),
                        description = stringResource(R.string.settings_pro_desc_free),
                        ctaLabel = stringResource(R.string.settings_pro_cta_free),
                        onClick = onProClick,
                    )
                }
            }

            // 6. Storage & Cache
            item {
                SettingsCardSection(
                    title = stringResource(R.string.settings_storage_title),
                    subtitle = stringResource(R.string.settings_storage_usage, settings.cacheSizeMb),
                    icon = Icons.Outlined.Storage,
                ) {
                    SnapSecondaryButton(
                        text = stringResource(R.string.settings_clear_cache),
                        leadingIcon = Icons.Outlined.CleaningServices,
                        enabled = settings.cacheSizeMb > 0,
                        onClick = { showClearCacheDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            // 7. 100% Private Ledger Card
            item {
                Surface(
                    shape = RoundedCornerShape(SnapSpacing.CardRadius),
                    color = extended.addressesContainer,
                    border = BorderStroke(1.dp, extended.onAddressesContainer.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(SnapSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Shield,
                                    contentDescription = null,
                                    tint = extended.onAddressesContainer,
                                )
                                Text(
                                    text = stringResource(R.string.settings_privacy_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = extended.onAddressesContainer,
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = extended.onAddressesContainer,
                            ) {
                                Text(
                                    text = stringResource(R.string.settings_privacy_badge),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = extended.addressesContainer,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                )
                            }
                        }

                        Text(
                            text = stringResource(R.string.settings_privacy_body),
                            style = MaterialTheme.typography.bodySmall,
                            color = extended.onAddressesContainer,
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            PrivacyMicroBadge(
                                icon = Icons.Outlined.CloudOff,
                                text = stringResource(R.string.settings_privacy_chip1),
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.72f),
                                contentColor = extended.onAddressesContainer,
                            )
                            PrivacyMicroBadge(
                                icon = Icons.Outlined.Lock,
                                text = stringResource(R.string.settings_privacy_chip2),
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.72f),
                                contentColor = extended.onAddressesContainer,
                            )
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    StatusBadge(text = stringResource(R.string.settings_footer_version))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.auth_footer_privacy),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }

    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            title = { Text(stringResource(R.string.settings_clear_cache_title)) },
            text = { Text(stringResource(R.string.settings_clear_cache_msg)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearCacheDialog = false
                        onClearCache()
                    },
                ) {
                    Text(
                        text = stringResource(R.string.action_confirm),
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheDialog = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }
}

@Composable
private fun PrivacyMicroBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    containerColor: Color,
    contentColor: Color,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsLightPreview() {
    SnapScribeTheme(themeMode = ThemeMode.LIGHT) {
        SettingsScreen(
            uiState = SettingsUiState(settings = AppSettings(textScale = TextScale.LARGE)),
            selectedLanguage = AppLanguage.INDONESIAN,
            onSelectTextScale = {},
            onToggleHighContrast = {},
            onSelectLanguage = {},
            onToggleIndexedFolder = { _, _ -> },
            onSelectThemeMode = {},
            onProClick = {},
            onClearCache = {},
            onProfileClick = {},
        )
    }
}
