package com.example

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.data.ConnectionType
import com.example.data.InstalledAppItem
import com.example.data.InstalledAppsRepository
import com.example.data.LauncherConfig
import com.example.data.LauncherPreferences
import com.example.data.NetworkMonitor
import com.example.ui.home.HomeScreen
import com.example.ui.onboarding.OnboardingScreen
import com.example.ui.saas.SaaSPaneScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

enum class LauncherScreen {
    ONBOARDING,
    HOME,
    SAAS_PANEL
}

class MainActivity : ComponentActivity() {

    private lateinit var launcherPreferences: LauncherPreferences
    private lateinit var installedAppsRepo: InstalledAppsRepository
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Force Landscape orientation as explicitly requested
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        
        enableEdgeToEdge()

        launcherPreferences = LauncherPreferences(this)
        installedAppsRepo = InstalledAppsRepository(this)
        networkMonitor = NetworkMonitor(this)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF140F24)
                ) {
                    ATVLauncherRoot(
                        preferences = launcherPreferences,
                        appsRepo = installedAppsRepo,
                        networkMonitor = networkMonitor,
                        onLaunchPackage = { pkg ->
                            val success = installedAppsRepo.launchApp(pkg)
                            if (!success) {
                                Toast.makeText(this, "Aplicativo $pkg não instalado", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onUninstallPackage = { pkg ->
                            installedAppsRepo.requestUninstall(pkg)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ATVLauncherRoot(
    preferences: LauncherPreferences,
    appsRepo: InstalledAppsRepository,
    networkMonitor: NetworkMonitor,
    onLaunchPackage: (String) -> Unit,
    onUninstallPackage: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val config by preferences.configFlow.collectAsState()
    val connectionType by networkMonitor.connectionType.collectAsState()

    var installedApps by remember { mutableStateOf<List<InstalledAppItem>>(emptyList()) }
    var isLoadingApps by remember { mutableStateOf(true) }

    // Screen state
    var currentScreen by remember(config.isFirstLaunchCompleted) {
        mutableStateOf(
            if (config.isFirstLaunchCompleted) LauncherScreen.HOME else LauncherScreen.ONBOARDING
        )
    }

    LaunchedEffect(Unit) {
        isLoadingApps = true
        installedApps = appsRepo.getInstalledApps()
        isLoadingApps = false
    }

    when (currentScreen) {
        LauncherScreen.ONBOARDING -> {
            OnboardingScreen(
                onFinishOnboarding = {
                    preferences.setFirstLaunchCompleted(true)
                    currentScreen = LauncherScreen.HOME
                }
            )
        }

        LauncherScreen.HOME -> {
            HomeScreen(
                config = config,
                connectionType = connectionType,
                installedApps = installedApps,
                isLoadingApps = isLoadingApps,
                onLaunchApp = onLaunchPackage,
                onUninstallApp = onUninstallPackage,
                onSelectLauncherTheme = { themeIdx ->
                    preferences.setThemes(themeIdx, config.dockThemeIndex)
                },
                onSelectDockTheme = { dockIdx ->
                    preferences.setThemes(config.launcherThemeIndex, dockIdx)
                }
            )
        }

        LauncherScreen.SAAS_PANEL -> {
            SaaSPaneScreen(
                currentConfig = config,
                onUpdateLogo = { text, url ->
                    preferences.updateLogo(text, url)
                },
                onUpdateVideoUrl = { url ->
                    preferences.updateVideoUrl(url)
                },
                onUpdateOta = { ver, url, notes ->
                    preferences.updateOta(ver, url, notes)
                },
                onUpdateDockApp = { index, item ->
                    preferences.updateDockApp(index, item)
                },
                onClose = {
                    currentScreen = LauncherScreen.HOME
                }
            )
        }
    }
}
