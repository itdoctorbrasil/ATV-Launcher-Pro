package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DockAppItem(
    val id: String,
    val name: String,
    val packageName: String,
    val defaultIconType: String // "unitv", "google_play", "prime_video", "netflix", "youtube_tv"
)

data class LauncherConfig(
    val isFirstLaunchCompleted: Boolean = false,
    val launcherThemeIndex: Int = 0, // 0..5
    val dockThemeIndex: Int = 0, // 0..5
    val logoText: String = "LOGO CUSTOMIZADA",
    val logoUrl: String = "",
    val videoUrl: String = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    val vpsHost: String = "177.153.68.5",
    val vpsPath: String = "/var/www/painel",
    val otaVersion: String = "1.0.0",
    val otaAvailableVersion: String = "1.0.1",
    val otaDownloadUrl: String = "http://177.153.68.5/painel/updates/atv-launcher-v1.0.1.apk",
    val otaChangelog: String = "• Otimização de performance no D-PAD\n• Compatibilidade ampliada com Android TV 12/13/14\n• Correções na dock de aplicativos",
    val dockApps: List<DockAppItem> = defaultDockApps
)

val defaultDockApps = listOf(
    DockAppItem("1", "UniTV", "com.unitv.app", "unitv"),
    DockAppItem("2", "Google Play", "com.android.vending", "google_play"),
    DockAppItem("3", "Prime Video", "com.amazon.amazonvideo.livingroom", "prime_video"),
    DockAppItem("4", "Netflix", "com.netflix.ninja", "netflix"),
    DockAppItem("5", "YouTube TV", "com.google.android.youtube.tv", "youtube_tv")
)

class LauncherPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("atv_launcher_prefs", Context.MODE_PRIVATE)

    private val _configFlow = MutableStateFlow(loadConfig())
    val configFlow: StateFlow<LauncherConfig> = _configFlow.asStateFlow()

    private fun loadConfig(): LauncherConfig {
        return LauncherConfig(
            isFirstLaunchCompleted = prefs.getBoolean(KEY_FIRST_LAUNCH, false),
            launcherThemeIndex = prefs.getInt(KEY_LAUNCHER_THEME, 0),
            dockThemeIndex = prefs.getInt(KEY_DOCK_THEME, 0),
            logoText = prefs.getString(KEY_LOGO_TEXT, "LOGO CUSTOMIZADA") ?: "LOGO CUSTOMIZADA",
            logoUrl = prefs.getString(KEY_LOGO_URL, "") ?: "",
            videoUrl = prefs.getString(KEY_VIDEO_URL, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4") ?: "",
            vpsHost = prefs.getString(KEY_VPS_HOST, "177.153.68.5") ?: "177.153.68.5",
            vpsPath = prefs.getString(KEY_VPS_PATH, "/var/www/painel") ?: "/var/www/painel",
            otaVersion = prefs.getString(KEY_OTA_VERSION, "1.0.0") ?: "1.0.0",
            otaAvailableVersion = prefs.getString(KEY_OTA_AVAIL_VER, "1.0.1") ?: "1.0.1",
            otaDownloadUrl = prefs.getString(KEY_OTA_URL, "http://177.153.68.5/painel/updates/atv-launcher-v1.0.1.apk") ?: "",
            otaChangelog = prefs.getString(KEY_OTA_CHANGELOG, "• Otimização de performance no D-PAD\n• Compatibilidade ampliada com Android TV") ?: ""
        )
    }

    fun setFirstLaunchCompleted(completed: Boolean) {
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, completed).apply()
        _configFlow.value = _configFlow.value.copy(isFirstLaunchCompleted = completed)
    }

    fun setThemes(launcherThemeIndex: Int, dockThemeIndex: Int) {
        prefs.edit()
            .putInt(KEY_LAUNCHER_THEME, launcherThemeIndex)
            .putInt(KEY_DOCK_THEME, dockThemeIndex)
            .apply()
        _configFlow.value = _configFlow.value.copy(
            launcherThemeIndex = launcherThemeIndex,
            dockThemeIndex = dockThemeIndex
        )
    }

    fun updateLogo(text: String, url: String) {
        prefs.edit()
            .putString(KEY_LOGO_TEXT, text)
            .putString(KEY_LOGO_URL, url)
            .apply()
        _configFlow.value = _configFlow.value.copy(logoText = text, logoUrl = url)
    }

    fun updateVideoUrl(url: String) {
        prefs.edit().putString(KEY_VIDEO_URL, url).apply()
        _configFlow.value = _configFlow.value.copy(videoUrl = url)
    }

    fun updateOta(version: String, apkUrl: String, changelog: String) {
        prefs.edit()
            .putString(KEY_OTA_AVAIL_VER, version)
            .putString(KEY_OTA_URL, apkUrl)
            .putString(KEY_OTA_CHANGELOG, changelog)
            .apply()
        _configFlow.value = _configFlow.value.copy(
            otaAvailableVersion = version,
            otaDownloadUrl = apkUrl,
            otaChangelog = changelog
        )
    }

    fun updateDockApp(index: Int, app: DockAppItem) {
        val currentApps = _configFlow.value.dockApps.toMutableList()
        if (index in currentApps.indices) {
            currentApps[index] = app
            _configFlow.value = _configFlow.value.copy(dockApps = currentApps)
        }
    }

    companion object {
        private const val KEY_FIRST_LAUNCH = "is_first_launch_done"
        private const val KEY_LAUNCHER_THEME = "launcher_theme_index"
        private const val KEY_DOCK_THEME = "dock_theme_index"
        private const val KEY_LOGO_TEXT = "logo_text"
        private const val KEY_LOGO_URL = "logo_url"
        private const val KEY_VIDEO_URL = "video_url"
        private const val KEY_VPS_HOST = "vps_host"
        private const val KEY_VPS_PATH = "vps_path"
        private const val KEY_OTA_VERSION = "ota_version"
        private const val KEY_OTA_AVAIL_VER = "ota_avail_ver"
        private const val KEY_OTA_URL = "ota_url"
        private const val KEY_OTA_CHANGELOG = "ota_changelog"
    }
}
