package com.genius.snapscribe.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.genius.snapscribe.R
import com.genius.snapscribe.domain.model.Session
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.domain.model.UserProfile
import com.genius.snapscribe.ui.components.SettingsCardSection
import com.genius.snapscribe.ui.components.SnapBackTopBar
import com.genius.snapscribe.ui.components.SnapPrimaryButton
import com.genius.snapscribe.ui.components.SnapSecondaryButton
import com.genius.snapscribe.ui.components.StatusBadge
import com.genius.snapscribe.ui.components.ToggleRow
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing
import com.genius.snapscribe.util.formatRupiahCompact

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToPro: (String) -> Unit,
    onSignedOut: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deletedMsg = stringResource(R.string.profile_local_deleted)

    ProfileScreen(
        uiState = uiState,
        onBack = onBack,
        onBiometricChange = viewModel::setBiometricLock,
        onSignInClick = onNavigateToAuth,
        onUpgradeProClick = { onNavigateToPro("profile") },
        onSignOutConfirm = { viewModel.signOut(onSignedOut = onSignedOut) },
        onDeleteLocalConfirm = {
            viewModel.deleteLocalData {
                onShowMessage(deletedMsg)
            }
        },
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onBack: () -> Unit,
    onBiometricChange: (Boolean) -> Unit,
    onSignInClick: () -> Unit,
    onUpgradeProClick: () -> Unit,
    onSignOutConfirm: () -> Unit,
    onDeleteLocalConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = SnapScribeTheme.extendedColors
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showDeleteLocalDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
    ) {
        SnapBackTopBar(
            title = stringResource(R.string.profile_title),
            onBack = onBack,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = SnapSpacing.md, vertical = SnapSpacing.xs)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            when (val session = uiState.session) {
                Session.Guest -> {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        border = BorderStroke(1.dp, extended.cardBorder),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            StatusBadge(text = stringResource(R.string.profile_guest_badge))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(extended.addressesContainer),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.WifiOff,
                                        contentDescription = null,
                                        tint = extended.onAddressesContainer,
                                    )
                                }
                                Text(
                                    text = stringResource(R.string.profile_guest_title),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.semantics { heading() },
                                )
                            }
                            Text(
                                text = stringResource(R.string.profile_guest_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            SnapPrimaryButton(
                                text = stringResource(R.string.profile_guest_cta),
                                onClick = onSignInClick,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }

                is Session.FreeAccount -> {
                    AccountIdentityCard(
                        user = session.user,
                        planLabel = stringResource(R.string.profile_plan_free),
                        isPro = false,
                    )
                }

                is Session.Pro -> {
                    AccountIdentityCard(
                        user = session.user,
                        planLabel = stringResource(R.string.profile_plan_pro),
                        isPro = true,
                    )
                }
            }

            // 3-Column Local Archive Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                ProfileStatTile(
                    icon = Icons.AutoMirrored.Outlined.ReceiptLong,
                    iconBg = extended.codesContainer,
                    iconTint = extended.onCodesContainer,
                    value = uiState.snapCount.toString(),
                    label = stringResource(R.string.profile_stat_snaps),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
                ProfileStatTile(
                    icon = Icons.Outlined.Folder,
                    iconBg = extended.addressesContainer,
                    iconTint = extended.onAddressesContainer,
                    value = uiState.folderCount.toString(),
                    label = stringResource(R.string.profile_stat_folders),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
                ProfileStatTile(
                    icon = Icons.Outlined.Payments,
                    iconBg = extended.receiptsContainer,
                    iconTint = extended.onReceiptsContainer,
                    value = formatRupiahCompact(uiState.monthlyTotalRupiah),
                    label = stringResource(R.string.profile_stat_month),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
            }

            // Personal Info & Security for Signed-In Accounts
            val signedInUser = when (val s = uiState.session) {
                Session.Guest -> null
                is Session.FreeAccount -> s.user
                is Session.Pro -> s.user
            }
            if (signedInUser != null) {
                SettingsCardSection(
                    title = stringResource(R.string.profile_security_section),
                    subtitle = signedInUser.email,
                    icon = Icons.Outlined.Shield,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Mail,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp),
                            )
                            Column {
                                Text(
                                    text = stringResource(R.string.profile_email_label),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Text(
                                    text = signedInUser.email,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                        if (signedInUser.emailVerified) {
                            StatusBadge(text = stringResource(R.string.profile_verified_pill))
                        }
                    }

                    HorizontalDivider(color = extended.cardBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                        Column {
                            Text(
                                text = stringResource(R.string.profile_phone_label),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = signedInUser.phone,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }

                    ToggleRow(
                        title = stringResource(R.string.profile_biometric_title),
                        description = stringResource(R.string.profile_biometric_desc),
                        checked = uiState.biometricLockEnabled,
                        onCheckedChange = onBiometricChange,
                        icon = Icons.Outlined.Fingerprint,
                    )
                }

                // Subscription Card
                SettingsCardSection(
                    title = stringResource(R.string.profile_subscription_section),
                    subtitle = stringResource(
                        if (uiState.session is Session.Pro) R.string.profile_plan_pro else R.string.profile_plan_free,
                    ),
                    icon = Icons.Outlined.AutoAwesome,
                    iconContainerColor = extended.receiptsContainer,
                    iconTint = extended.onReceiptsContainer,
                ) {
                    SnapSecondaryButton(
                        text = stringResource(
                            if (uiState.session is Session.Pro) {
                                R.string.settings_pro_cta_active
                            } else {
                                R.string.profile_upgrade_cta
                            },
                        ),
                        onClick = onUpgradeProClick,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            // Local Storage Section
            SettingsCardSection(
                title = stringResource(R.string.profile_storage_section),
                subtitle = stringResource(R.string.profile_storage_desc, uiState.cacheSizeMb),
                icon = Icons.Outlined.Storage,
            ) {
                SnapSecondaryButton(
                    text = stringResource(R.string.profile_delete_local),
                    leadingIcon = Icons.Outlined.DeleteOutline,
                    contentColor = MaterialTheme.colorScheme.error,
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.45f),
                    borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.35f),
                    onClick = { showDeleteLocalDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            if (signedInUser != null) {
                SnapSecondaryButton(
                    text = stringResource(R.string.profile_sign_out),
                    leadingIcon = Icons.AutoMirrored.Rounded.ExitToApp,
                    onClick = { showSignOutDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = { Text(stringResource(R.string.profile_sign_out_title)) },
            text = { Text(stringResource(R.string.profile_sign_out_msg)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSignOutDialog = false
                        onSignOutConfirm()
                    },
                ) {
                    Text(
                        text = stringResource(R.string.profile_sign_out),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }

    if (showDeleteLocalDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteLocalDialog = false },
            title = { Text(stringResource(R.string.profile_delete_local_title)) },
            text = { Text(stringResource(R.string.profile_delete_local_msg)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteLocalDialog = false
                        onDeleteLocalConfirm()
                    },
                ) {
                    Text(
                        text = stringResource(R.string.action_delete),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteLocalDialog = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }
}

@Composable
private fun AccountIdentityCard(
    user: UserProfile,
    planLabel: String,
    isPro: Boolean,
) {
    val extended = SnapScribeTheme.extendedColors
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, extended.cardBorder),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(modifier = Modifier.size(72.dp)) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.matchParentSize(),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = user.initials,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Verified,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }

            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                text = "${user.handle} • ${user.memberSince}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            StatusBadge(
                text = planLabel,
                containerColor = if (isPro) extended.receiptsContainer else extended.successContainer,
                contentColor = if (isPro) extended.onReceiptsContainer else extended.onSuccess,
            )
        }
    }
}

@Composable
private fun ProfileStatTile(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    val extended = SnapScribeTheme.extendedColors
    Surface(
        shape = RoundedCornerShape(SnapSpacing.CardRadius),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, extended.cardBorder),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileGuestPreview() {
    SnapScribeTheme(themeMode = ThemeMode.LIGHT) {
        ProfileScreen(
            uiState = ProfileUiState(session = Session.Guest, snapCount = 20, monthlyTotalRupiah = 1_448_500L),
            onBack = {},
            onBiometricChange = {},
            onSignInClick = {},
            onUpgradeProClick = {},
            onSignOutConfirm = {},
            onDeleteLocalConfirm = {},
        )
    }
}
