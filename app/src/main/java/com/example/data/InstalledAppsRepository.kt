package com.example.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class InstalledAppItem(
    val packageName: String,
    val label: String,
    val icon: Drawable?,
    val isSystemApp: Boolean
)

class InstalledAppsRepository(private val context: Context) {

    suspend fun getInstalledApps(): List<InstalledAppItem> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val leanbackIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        }

        val launcherApps = pm.queryIntentActivities(mainIntent, 0)
        val tvApps = pm.queryIntentActivities(leanbackIntent, 0)

        val allResolveInfos = (launcherApps + tvApps).distinctBy { it.activityInfo.packageName }

        val currentPackage = context.packageName

        allResolveInfos
            .filter { it.activityInfo.packageName != currentPackage }
            .mapNotNull { resolveInfo ->
                try {
                    val appInfo = resolveInfo.activityInfo.applicationInfo
                    val label = resolveInfo.loadLabel(pm).toString().ifBlank {
                        appInfo.loadLabel(pm).toString()
                    }
                    val icon = resolveInfo.loadIcon(pm) ?: appInfo.loadIcon(pm)
                    val isSystem = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0

                    InstalledAppItem(
                        packageName = resolveInfo.activityInfo.packageName,
                        label = label,
                        icon = icon,
                        isSystemApp = isSystem
                    )
                } catch (e: Exception) {
                    null
                }
            }
            .sortedBy { it.label.lowercase() }
    }

    fun launchApp(packageName: String): Boolean {
        val pm = context.packageManager
        val launchIntent = pm.getLeanbackLaunchIntentForPackage(packageName)
            ?: pm.getLaunchIntentForPackage(packageName)
        return if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
            true
        } else {
            false
        }
    }

    fun requestUninstall(packageName: String) {
        val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE).apply {
            data = Uri.parse("package:$packageName")
            putExtra(Intent.EXTRA_RETURN_RESULT, true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback for older or customized Android TV builds
            val fallbackIntent = Intent(Intent.ACTION_DELETE).apply {
                data = Uri.parse("package:$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallbackIntent)
        }
    }
}
