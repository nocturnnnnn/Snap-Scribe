package com.genius.snapscribe.ui.screens.camera

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FlashAuto
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.genius.snapscribe.R
import com.genius.snapscribe.data.repository.SessionRepository
import com.genius.snapscribe.data.repository.SnapRepository
import com.genius.snapscribe.data.sample.SampleSnaps
import com.genius.snapscribe.domain.model.Session
import com.genius.snapscribe.domain.model.ThemeMode
import com.genius.snapscribe.ui.components.FilterPillChip
import com.genius.snapscribe.ui.components.ProLockBadge
import com.genius.snapscribe.ui.components.SnapBackTopBar
import com.genius.snapscribe.ui.components.StatusBadge
import com.genius.snapscribe.ui.theme.SnapScribeTheme
import com.genius.snapscribe.ui.theme.SnapSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class FlashMode { AUTO, ON, OFF }
enum class CaptureMode { DOCUMENT, HANDWRITTEN, WIFI }

@Composable
fun CameraCaptureRoute(
    snapRepository: SnapRepository,
    sessionRepository: SessionRepository,
    onBack: () -> Unit,
    onSnapCaptured: (String) -> Unit,
    onProClick: (String) -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val session by sessionRepository.session.collectAsStateWithLifecycle()
    val isPro = session is Session.Pro
    val scope = rememberCoroutineScope()
    var isProcessing by remember { mutableStateOf(false) }
    val galleryMessage = stringResource(R.string.camera_gallery_demo)

    CameraCaptureScreen(
        isPro = isPro,
        isProcessing = isProcessing,
        onBack = onBack,
        onGalleryClick = { onShowMessage(galleryMessage) },
        onLockedModeClick = { onProClick("handwritten") },
        onShutterClick = { mode ->
            if (!isProcessing) {
                isProcessing = true
                scope.launch {
                    delay(850L)
                    val newSnap = SampleSnaps.createScannedSampleSnap(mode.name)
                    snapRepository.addSnap(newSnap)
                    isProcessing = false
                    onSnapCaptured(newSnap.id)
                }
            }
        },
    )
}

@Composable
fun CameraCaptureScreen(
    isPro: Boolean,
    isProcessing: Boolean,
    onBack: () -> Unit,
    onGalleryClick: () -> Unit,
    onLockedModeClick: () -> Unit,
    onShutterClick: (CaptureMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = SnapScribeTheme.extendedColors
    var flashMode by remember { mutableStateOf(FlashMode.AUTO) }
    var showGrid by remember { mutableStateOf(true) }
    var multiSlip by remember { mutableStateOf(false) }
    var captureMode by remember { mutableStateOf(CaptureMode.DOCUMENT) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
    ) {
        SnapBackTopBar(
            title = stringResource(R.string.camera_title),
            onBack = onBack,
        )

        // Top camera status & utility chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SnapSpacing.md, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatusBadge(text = stringResource(R.string.camera_badge_ocr))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val flashIcon = when (flashMode) {
                    FlashMode.AUTO -> Icons.Outlined.FlashAuto
                    FlashMode.ON -> Icons.Outlined.FlashOn
                    FlashMode.OFF -> Icons.Outlined.FlashOff
                }
                val flashLabel = when (flashMode) {
                    FlashMode.AUTO -> stringResource(R.string.camera_flash_auto)
                    FlashMode.ON -> stringResource(R.string.camera_flash_on)
                    FlashMode.OFF -> stringResource(R.string.camera_flash_off)
                }
                FilterPillChip(
                    label = flashLabel,
                    icon = flashIcon,
                    selected = flashMode != FlashMode.OFF,
                    onClick = {
                        flashMode = when (flashMode) {
                            FlashMode.AUTO -> FlashMode.ON
                            FlashMode.ON -> FlashMode.OFF
                            FlashMode.OFF -> FlashMode.AUTO
                        }
                    },
                )
                FilterPillChip(
                    label = stringResource(R.string.camera_grid),
                    icon = Icons.Outlined.GridOn,
                    selected = showGrid,
                    onClick = { showGrid = !showGrid },
                )
            }
        }

        // Simulated Viewfinder Frame
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = SnapSpacing.md, vertical = 8.dp)
                .clip(RoundedCornerShape(22.dp)),
        ) {
            Image(
                painter = painterResource(id = R.drawable.sample_camera_preview),
                contentDescription = stringResource(R.string.camera_title),
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )

            // Subtle dark scrim + bracket framing + optional grid
            val bracketColor = MaterialTheme.colorScheme.primary
            Canvas(modifier = Modifier.matchParentSize()) {
                if (showGrid) {
                    val gridColor = Color.White.copy(alpha = 0.25f)
                    drawLine(gridColor, Offset(size.width / 3f, 0f), Offset(size.width / 3f, size.height), 1.dp.toPx())
                    drawLine(gridColor, Offset(size.width * 2f / 3f, 0f), Offset(size.width * 2f / 3f, size.height), 1.dp.toPx())
                    drawLine(gridColor, Offset(0f, size.height / 3f), Offset(size.width, size.height / 3f), 1.dp.toPx())
                    drawLine(gridColor, Offset(0f, size.height * 2f / 3f), Offset(size.width, size.height * 2f / 3f), 1.dp.toPx())
                }

                val inset = 28.dp.toPx()
                val arm = 32.dp.toPx()
                val stroke = 4.dp.toPx()

                // Top-left
                drawLine(bracketColor, Offset(inset, inset), Offset(inset + arm, inset), stroke, StrokeCap.Round)
                drawLine(bracketColor, Offset(inset, inset), Offset(inset, inset + arm), stroke, StrokeCap.Round)
                // Top-right
                drawLine(bracketColor, Offset(size.width - inset, inset), Offset(size.width - inset - arm, inset), stroke, StrokeCap.Round)
                drawLine(bracketColor, Offset(size.width - inset, inset), Offset(size.width - inset, inset + arm), stroke, StrokeCap.Round)
                // Bottom-left
                drawLine(bracketColor, Offset(inset, size.height - inset), Offset(inset + arm, size.height - inset), stroke, StrokeCap.Round)
                drawLine(bracketColor, Offset(inset, size.height - inset), Offset(inset, size.height - inset - arm), stroke, StrokeCap.Round)
                // Bottom-right
                drawLine(bracketColor, Offset(size.width - inset, size.height - inset), Offset(size.width - inset - arm, size.height - inset), stroke, StrokeCap.Round)
                drawLine(bracketColor, Offset(size.width - inset, size.height - inset), Offset(size.width - inset, size.height - inset - arm), stroke, StrokeCap.Round)
            }

            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xCC1E1B18),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF9EE0B2),
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = stringResource(R.string.camera_detect_hint),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFFF5E6D3),
                    )
                }
            }

            if (isProcessing) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color(0xB8181210)),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Text(
                            text = stringResource(R.string.camera_processing),
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFFF5E6D3),
                        )
                    }
                }
            }
        }

        // Bottom Controls Tray
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            border = BorderStroke(1.dp, extended.cardBorder),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = SnapSpacing.md, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterPillChip(
                        label = stringResource(R.string.camera_mode_doc),
                        selected = captureMode == CaptureMode.DOCUMENT,
                        onClick = { captureMode = CaptureMode.DOCUMENT },
                    )
                    FilterPillChip(
                        label = stringResource(R.string.camera_mode_handwritten),
                        icon = if (isPro) Icons.Outlined.AutoAwesome else Icons.Outlined.Lock,
                        selected = captureMode == CaptureMode.HANDWRITTEN,
                        onClick = {
                            if (isPro) {
                                captureMode = CaptureMode.HANDWRITTEN
                            } else {
                                onLockedModeClick()
                            }
                        },
                    )
                    FilterPillChip(
                        label = stringResource(R.string.camera_mode_wifi),
                        selected = captureMode == CaptureMode.WIFI,
                        onClick = { captureMode = CaptureMode.WIFI },
                    )
                }

                // Gallery + Shutter + Single/Multi Slip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 64.dp, minHeight = SnapSpacing.MinTouchTarget)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(role = Role.Button, onClick = onGalleryClick)
                            .padding(6.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoLibrary,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = stringResource(R.string.camera_gallery),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    val shutterCd = stringResource(R.string.camera_shutter_cd)
                    Surface(
                        onClick = { onShutterClick(captureMode) },
                        enabled = !isProcessing,
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        border = BorderStroke(4.dp, MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .size(72.dp)
                            .semantics { contentDescription = shutterCd },
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.PhotoCamera,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(30.dp),
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 64.dp, minHeight = SnapSpacing.MinTouchTarget)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(role = Role.Button, onClick = { multiSlip = !multiSlip })
                            .padding(6.dp),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                            contentDescription = null,
                            tint = if (multiSlip) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = stringResource(
                                if (multiSlip) R.string.camera_multi_slip else R.string.camera_single_slip,
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    border = BorderStroke(1.dp, extended.cardBorder),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ReceiptLong,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.camera_ledger_ready),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = stringResource(R.string.camera_ledger_desc),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        if (!isPro && captureMode == CaptureMode.HANDWRITTEN) {
                            ProLockBadge()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CameraDarkPreview() {
    SnapScribeTheme(themeMode = ThemeMode.DARK) {
        CameraCaptureScreen(
            isPro = false,
            isProcessing = false,
            onBack = {},
            onGalleryClick = {},
            onLockedModeClick = {},
            onShutterClick = {},
        )
    }
}
