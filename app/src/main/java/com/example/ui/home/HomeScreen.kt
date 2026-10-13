package com.example.ui.home

import android.text.format.DateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.ConnectionType
import com.example.data.DockAppItem
import com.example.data.InstalledAppItem
import com.example.data.LauncherConfig
import com.example.data.NetworkMonitor
import com.example.ui.components.VideoPlayerView
import com.example.ui.components.dpadFocusable
import com.example.ui.theme.DockThemes
import com.example.ui.theme.LauncherThemes
import kotlinx.coroutines.delay
import java.util.Date

@Composable
fun HomeScreen(
    config: LauncherConfig,
    connectionType: ConnectionType,
    installedApps: List<InstalledAppItem>,
    isLoadingApps: Boolean,
    onLaunchApp: (String) -> Unit,
    onUninstallApp: (String) -> Unit,
    onSelectLauncherTheme: (Int) -> Unit,
    onSelectDockTheme: (Int) -> Unit,
    onOpenSaaSPane: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showCustomizationDialog by remember { mutableStateOf(false) }
    var showAppsDrawer by remember { mutableStateOf(false) }
    var showOtaDialog by remember { mutableStateOf(false) }

    // Live Clock State
    var currentTimeString by remember {
        mutableStateOf(DateFormat.format("HH:mm", Date()).toString())
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTimeString = DateFormat.format("HH:mm", Date()).toString()
            delay(1000)
        }
    }

    // Active theme colors
    val launcherTheme = LauncherThemes.getOrElse(config.launcherThemeIndex) { LauncherThemes[0] }
    val dockTheme = DockThemes.getOrElse(config.dockThemeIndex) { DockThemes[0] }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            launcherTheme.backgroundColor,
            launcherTheme.gradientEndColor
        )
    )

    // Bouncing animation for the down arrow below the dock
    val infiniteTransition = rememberInfiniteTransition(label = "arrowBounce")
    val arrowOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arrowOffsetY"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(horizontal = 32.dp, vertical = 18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ================= 1. TOP BAR =================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Client Logo (defined via SaaS panel / text or image)
                Box(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (config.logoUrl.isNotBlank()) {
                        AsyncImage(
                            model = config.logoUrl,
                            contentDescription = "Logo Customizada",
                            modifier = Modifier.height(28.dp)
                        )
                    } else {
                        Text(
                            text = config.logoText,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp
                        )
                    }
                }

                // Center: Digital Clock (e.g. 12:01)
                Text(
                    text = currentTimeString,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                // Right: Action Icons (Customization, Network, Settings) - Clean without Painel button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Customization Icon (Arrows icon as shown in Home_Launcher.png)
                    Box(
                        modifier = Modifier
                            .dpadFocusable(
                                onClick = { showCustomizationDialog = true },
                                shape = CircleShape,
                                focusedBorderColor = Color.White
                            )
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CompareArrows,
                            contentDescription = "Customização de Temas",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Network Icon (Wi-Fi or Ethernet or Offline)
                    Box(
                        modifier = Modifier
                            .dpadFocusable(
                                onClick = { NetworkMonitor.openNetworkSettings(context) },
                                shape = CircleShape,
                                focusedBorderColor = Color.White
                            )
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        val networkIcon = when (connectionType) {
                            ConnectionType.WIFI -> Icons.Default.Wifi
                            ConnectionType.ETHERNET -> Icons.Default.Lan
                            ConnectionType.CELLULAR -> Icons.Default.Wifi
                            ConnectionType.NONE -> Icons.Default.WifiOff
                        }
                        Icon(
                            imageVector = networkIcon,
                            contentDescription = "Rede e Conexões",
                            tint = if (connectionType == ConnectionType.NONE) Color(0xFFEF4444) else Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Settings Icon (Gear)
                    Box(
                        modifier = Modifier
                            .dpadFocusable(
                                onClick = { NetworkMonitor.openGeneralSettings(context) },
                                shape = CircleShape,
                                focusedBorderColor = Color.White
                            )
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configurações do Dispositivo",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // ================= 2. CENTER VIDEO PLAYER =================
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .weight(1f)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                VideoPlayerView(
                    videoUrl = config.videoUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.95f)
                )
            }

            // ================= 3. BOTTOM DOCK & ARROW =================
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Fixed 5-App Dock
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, dockTheme.borderColor, RoundedCornerShape(18.dp))
                        .background(dockTheme.backgroundColor)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        config.dockApps.take(5).forEach { appItem ->
                            DockAppCard(
                                appItem = appItem,
                                textColor = dockTheme.textColor,
                                onClick = { onLaunchApp(appItem.packageName) },
                                onNavigateDown = { showAppsDrawer = true }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Animated down arrow: automatically opens the installed apps drawer when focused
                Box(
                    modifier = Modifier
                        .offset(y = arrowOffsetY.dp)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                showAppsDrawer = true
                            }
                        }
                        .dpadFocusable(
                            onClick = { showAppsDrawer = true },
                            shape = CircleShape,
                            focusedBorderColor = Color.White
                        )
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0284C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Abrir Grade de Apps Instalados",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Customization Dialog
        if (showCustomizationDialog) {
            CustomizationDialog(
                currentLauncherThemeIndex = config.launcherThemeIndex,
                currentDockThemeIndex = config.dockThemeIndex,
                onSelectLauncherTheme = onSelectLauncherTheme,
                onSelectDockTheme = onSelectDockTheme,
                onDismiss = { showCustomizationDialog = false }
            )
        }

        // Installed Apps Drawer Overlay
        AnimatedVisibility(
            visible = showAppsDrawer,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            AppsDrawerSheet(
                apps = installedApps,
                isLoading = isLoadingApps,
                onLaunchApp = onLaunchApp,
                onUninstallApp = onUninstallApp,
                onClose = { showAppsDrawer = false }
            )
        }
    }
}

@Composable
private fun DockAppCard(
    appItem: DockAppItem,
    textColor: Color,
    onClick: () -> Unit,
    onNavigateDown: () -> Unit
) {
    Box(
        modifier = Modifier
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.DirectionDown) {
                    onNavigateDown()
                    true
                } else {
                    false
                }
            }
            .dpadFocusable(
                onClick = onClick,
                shape = RoundedCornerShape(14.dp),
                focusedBorderColor = Color(0xFFA78BFA),
                focusedScale = 1.12f
            )
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Transparent)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        when (appItem.defaultIconType) {
            "unitv" -> UniTvBadge(textColor)
            "google_play" -> GooglePlayBadge()
            "prime_video" -> PrimeVideoBadge()
            "netflix" -> NetflixBadge()
            "youtube_tv" -> YouTubeTvBadge()
            else -> GenericAppBadge(appItem.name, textColor)
        }
    }
}

// Brand Badges matching the visual references in Home_Launcher.png
@Composable
private fun UniTvBadge(textColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFFEF4444), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color(0xFFF97316),
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = "UniTV",
            color = textColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun GooglePlayBadge() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color(0xFF00C853),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "Google Play",
            color = Color(0xFF374151),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PrimeVideoBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0F172A))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "prime video",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .width(42.dp)
                    .height(2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(Color(0xFF38BDF8))
            )
        }
    }
}

@Composable
private fun NetflixBadge() {
    Text(
        text = "NETFLIX",
        color = Color(0xFFE50914),
        fontSize = 17.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.sp
    )
}

@Composable
private fun YouTubeTvBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFF0000)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
            Text(
                text = "YouTube TV",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GenericAppBadge(name: String, textColor: Color) {
    Text(
        text = name,
        color = textColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}
