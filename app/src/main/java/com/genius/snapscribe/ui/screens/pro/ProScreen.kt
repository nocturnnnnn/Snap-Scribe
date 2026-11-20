package com.genius.snapscribe.ui.screens.pro

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.HorizontalRule
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.genius.snapscribe.R
import com.genius.snapscribe.domain.model.BillingCycle
import com.genius.snapscribe.domain.model.Session
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.ui.components.SegmentedSelector
import com.genius.snapscribe.ui.components.SnapBackTopBar
import com.genius.snapscribe.ui.components.SnapPrimaryButton
import com.genius.snapscribe.ui.components.SnapSecondaryButton
import com.genius.snapscribe.ui.components.StatusBadge
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing

private data class ComparisonRow(
    @param:StringRes val featureRes: Int,
    val includedInFree: Boolean,
    val includedInPro: Boolean = true,
)

private val comparisonFeatures = listOf(
    ComparisonRow(R.string.pro_feat_ocr, includedInFree = true),
    ComparisonRow(R.string.pro_feat_categories, includedInFree = true),
    ComparisonRow(R.string.pro_feat_search, includedInFree = true),
    ComparisonRow(R.string.pro_feat_actions, includedInFree = true),
    ComparisonRow(R.string.pro_feat_a11y, includedInFree = true),
    ComparisonRow(R.string.pro_feat_cloud, includedInFree = false),
    ComparisonRow(R.string.pro_feat_export, includedInFree = false),
    ComparisonRow(R.string.pro_feat_expiry, includedInFree = false),
    ComparisonRow(R.string.pro_feat_handwritten, includedInFree = false),
    ComparisonRow(R.string.pro_feat_early, includedInFree = false),
)

@Composable
fun ProRoute(
    viewModel: ProViewModel,
    onBack: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val trialMsg = stringResource(R.string.pro_trial_started_msg)
    val subMsg = stringResource(R.string.pro_subscribed_msg)
    val cancelMsg = stringResource(R.string.pro_cancelled_msg)

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                ProEvent.TrialStarted -> onShowMessage(trialMsg)
                ProEvent.Subscribed -> onShowMessage(subMsg)
                ProEvent.Cancelled -> onShowMessage(cancelMsg)
            }
        }
    }

    ProScreen(
        uiState = uiState,
        onBack = onBack,
        onSelectCycle = viewModel::selectCycle,
        onSignInClick = onNavigateToAuth,
        onStartTrialClick = viewModel::startTrial,
        onSubscribeClick = viewModel::subscribe,
        onCancelSubscriptionClick = viewModel::cancelSubscription,
    )
}

@Composable
fun ProScreen(
    uiState: ProUiState,
    onBack: () -> Unit,
    onSelectCycle: (BillingCycle) -> Unit,
    onSignInClick: () -> Unit,
    onStartTrialClick: () -> Unit,
    onSubscribeClick: () -> Unit,
    onCancelSubscriptionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = SnapScribeTheme.extendedColors
    val priceText = stringResource(
        if (uiState.selectedCycle == BillingCycle.MONTHLY) {
            R.string.pro_price_monthly
        } else {
            R.string.pro_price_yearly
        },
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
    ) {
        SnapBackTopBar(
            title = stringResource(R.string.pro_title),
            onBack = onBack,
        )

        LazyColumn(
            contentPadding = PaddingValues(
                start = SnapSpacing.md,
                end = SnapSpacing.md,
                top = SnapSpacing.xs,
                bottom = 20.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f),
        ) {
            // Warm editorial hero card (paper stationery, no neon gradients)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = extended.receiptsContainer.copy(alpha = if (extended.isDark) 0.55f else 0.72f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = stringResource(R.string.pro_hero_badge),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        Text(
                            text = stringResource(R.string.pro_hero_title),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.semantics { heading() },
                        )
                        Text(
                            text = stringResource(R.string.pro_hero_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            // Billing Cycle Selector + Price
            if (uiState.ctaState != ProCtaState.ALREADY_PRO) {
                item {
                    Surface(
                        shape = RoundedCornerShape(SnapSpacing.CardRadius),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        border = BorderStroke(1.dp, extended.cardBorder),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(SnapSpacing.md),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            SegmentedSelector(
                                options = BillingCycle.entries,
                                selectedOption = uiState.selectedCycle,
                                labelProvider = { cycle ->
                                    when (cycle) {
                                        BillingCycle.MONTHLY -> stringResource(R.string.pro_billing_monthly)
                                        BillingCycle.YEARLY -> "${stringResource(R.string.pro_billing_yearly)} • ${stringResource(R.string.pro_yearly_save)}"
                                    }
                                },
                                onSelect = onSelectCycle,
                            )
                            Text(
                                text = priceText,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }

            // Active Pro card if already subscribed
            if (uiState.session is Session.Pro) {
                item {
                    Surface(
                        shape = RoundedCornerShape(SnapSpacing.CardRadius),
                        color = extended.successContainer,
                        border = BorderStroke(1.dp, extended.onSuccess.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(SnapSpacing.md),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = stringResource(R.string.pro_active_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = extended.onSuccess,
                                )
                                if (uiState.session.inTrial) {
                                    StatusBadge(text = stringResource(R.string.pro_active_trial_badge))
                                }
                            }
                            Text(
                                text = stringResource(R.string.pro_active_renews, uiState.session.renewsAt),
                                style = MaterialTheme.typography.bodySmall,
                                color = extended.onSuccess,
                            )
                        }
                    }
                }
            }

            // 7-Day Free Trial Timeline
            if (uiState.showTrialTimeline) {
                item {
                    Surface(
                        shape = RoundedCornerShape(SnapSpacing.CardRadius),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        border = BorderStroke(1.dp, extended.cardBorder),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(SnapSpacing.md),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.NotificationsActive,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                                Text(
                                    text = stringResource(R.string.pro_trial_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.semantics { heading() },
                                )
                            }
                            TrialTimelineStep(stepNumber = "0", text = stringResource(R.string.pro_trial_day0))
                            TrialTimelineStep(stepNumber = "5", text = stringResource(R.string.pro_trial_day5))
                            TrialTimelineStep(stepNumber = "7", text = stringResource(R.string.pro_trial_day7))

                            HorizontalDivider(color = extended.cardBorder)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Shield,
                                    contentDescription = null,
                                    tint = extended.onSuccess,
                                    modifier = Modifier.size(18.dp),
                                )
                                Text(
                                    text = stringResource(R.string.pro_trial_reassurance),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }

            // Transparent Free vs Pro Comparison Table
            item {
                Surface(
                    shape = RoundedCornerShape(SnapSpacing.CardRadius),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    border = BorderStroke(1.dp, extended.cardBorder),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(SnapSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.pro_compare_title),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.semantics { heading() },
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.pro_col_feature),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f),
                            )
                            Text(
                                text = stringResource(R.string.pro_col_free),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(52.dp),
                            )
                            Text(
                                text = stringResource(R.string.pro_col_pro),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(52.dp),
                            )
                        }

                        HorizontalDivider(color = extended.cardBorder)

                        comparisonFeatures.forEachIndexed { index, row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = stringResource(row.featureRes),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f),
                                )
                                Box(
                                    modifier = Modifier.width(52.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        imageVector = if (row.includedInFree) {
                                            Icons.Outlined.Check
                                        } else {
                                            Icons.Outlined.HorizontalRule
                                        },
                                        contentDescription = null,
                                        tint = if (row.includedInFree) {
                                            extended.onSuccess
                                        } else {
                                            MaterialTheme.colorScheme.outline
                                        },
                                        modifier = Modifier.size(18.dp),
                                    )
                                }
                                Box(
                                    modifier = Modifier.width(52.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp),
                                    )
                                }
                            }
                            if (index < comparisonFeatures.lastIndex) {
                                HorizontalDivider(color = extended.cardBorder.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }

        // Bottom contextual CTA
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column {
                HorizontalDivider(color = extended.cardBorder)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SnapSpacing.md, vertical = 12.dp),
                ) {
                    when (uiState.ctaState) {
                        ProCtaState.SIGN_IN_REQUIRED -> {
                            SnapPrimaryButton(
                                text = stringResource(R.string.pro_cta_guest),
                                onClick = onSignInClick,
                                loading = uiState.isLoading,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        ProCtaState.START_FREE_TRIAL -> {
                            SnapPrimaryButton(
                                text = stringResource(R.string.pro_cta_trial),
                                onClick = onStartTrialClick,
                                loading = uiState.isLoading,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        ProCtaState.SUBSCRIBE_DIRECT -> {
                            SnapPrimaryButton(
                                text = stringResource(R.string.pro_cta_subscribe, priceText),
                                onClick = onSubscribeClick,
                                loading = uiState.isLoading,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        ProCtaState.ALREADY_PRO -> {
                            SnapSecondaryButton(
                                text = stringResource(R.string.pro_cancel_demo),
                                loading = uiState.isLoading,
                                contentColor = MaterialTheme.colorScheme.error,
                                borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.4f),
                                onClick = onCancelSubscriptionClick,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrialTimelineStep(
    stepNumber: String,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stepNumber,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProLightPreview() {
    SnapScribeTheme(themeMode = ThemeMode.LIGHT) {
        ProScreen(
            uiState = ProUiState(
                ctaState = ProCtaState.START_FREE_TRIAL,
                showTrialTimeline = true,
            ),
            onBack = {},
            onSelectCycle = {},
            onSignInClick = {},
            onStartTrialClick = {},
            onSubscribeClick = {},
            onCancelSubscriptionClick = {},
        )
    }
}
