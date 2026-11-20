package com.genius.snapscribe.ui.screens.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.FolderSpecial
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.genius.snapscribe.R
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.ui.components.SnapPrimaryButton
import com.genius.snapscribe.ui.components.StatusBadge
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = SnapScribeTheme.extendedColors
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = SnapSpacing.md, vertical = SnapSpacing.md),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        // Top brand pill
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            border = BorderStroke(1.dp, extended.cardBorder),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_snapscribe_logo),
                    contentDescription = stringResource(R.string.cd_app_logo),
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp)),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        // Hero illustration card
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = BorderStroke(1.dp, extended.cardBorder),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .aspectRatio(1.55f)
                    .clip(RoundedCornerShape(16.dp)),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_welcome),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize(),
                )
                StatusBadge(
                    text = stringResource(R.string.welcome_badge_vault),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp),
                )
            }
        }

        // Editorial headline & subtitle
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.welcome_headline),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                text = stringResource(R.string.welcome_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        // Feature summary card
        Surface(
            shape = RoundedCornerShape(SnapSpacing.CardRadius),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            border = BorderStroke(1.dp, extended.cardBorder),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(SnapSpacing.md)) {
                WelcomeFeatureRow(
                    icon = Icons.Outlined.DocumentScanner,
                    iconBg = extended.codesContainer,
                    iconTint = extended.onCodesContainer,
                    title = stringResource(R.string.welcome_feat1_title),
                    description = stringResource(R.string.welcome_feat1_desc),
                )
                HorizontalDivider(
                    color = extended.cardBorder,
                    modifier = Modifier.padding(vertical = 12.dp),
                )
                WelcomeFeatureRow(
                    icon = Icons.Outlined.FolderSpecial,
                    iconBg = extended.receiptsContainer,
                    iconTint = extended.onReceiptsContainer,
                    title = stringResource(R.string.welcome_feat2_title),
                    description = stringResource(R.string.welcome_feat2_desc),
                )
                HorizontalDivider(
                    color = extended.cardBorder,
                    modifier = Modifier.padding(vertical = 12.dp),
                )
                WelcomeFeatureRow(
                    icon = Icons.Outlined.VerifiedUser,
                    iconBg = extended.addressesContainer,
                    iconTint = extended.onAddressesContainer,
                    title = stringResource(R.string.welcome_feat3_title),
                    description = stringResource(R.string.welcome_feat3_desc),
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SnapPrimaryButton(
                text = stringResource(R.string.welcome_cta_start),
                trailingIcon = Icons.AutoMirrored.Rounded.ArrowForward,
                onClick = onStartClick,
                modifier = Modifier.fillMaxWidth(),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = SnapSpacing.MinTouchTarget)
                    .clip(RoundedCornerShape(50))
                    .clickable(role = Role.Button, onClick = onSkipClick)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.welcome_skip),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun WelcomeFeatureRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    description: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp),
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeLightPreview() {
    SnapScribeTheme(themeMode = ThemeMode.LIGHT) {
        WelcomeScreen(onStartClick = {}, onSkipClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeDarkPreview() {
    SnapScribeTheme(themeMode = ThemeMode.DARK) {
        WelcomeScreen(onStartClick = {}, onSkipClick = {})
    }
}
