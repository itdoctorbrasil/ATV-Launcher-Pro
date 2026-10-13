package com.example.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.provider.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ConnectionType {
    WIFI,
    ETHERNET,
    CELLULAR,
    NONE
}

class NetworkMonitor(private val context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _connectionType = MutableStateFlow(determineCurrentConnection())
    val connectionType: StateFlow<ConnectionType> = _connectionType.asStateFlow()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            updateConnection()
        }

        override fun onLost(network: Network) {
            updateConnection()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            updateConnection()
        }
    }

    init {
        try {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        } catch (e: Exception) {
            // fallback
        }
    }

    private fun updateConnection() {
        _connectionType.value = determineCurrentConnection()
    }

    private fun determineCurrentConnection(): ConnectionType {
        return try {
            val activeNetwork = connectivityManager.activeNetwork ?: return ConnectionType.NONE
            val caps = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return ConnectionType.NONE
            when {
                caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
                caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectionType.ETHERNET
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.CELLULAR
                else -> ConnectionType.NONE
            }
        } catch (e: Exception) {
            ConnectionType.NONE
        }
    }

    companion object {
        fun openNetworkSettings(context: Context) {
            val intentList = listOf(
                // Android TV Network Settings
                Intent().apply {
                    component = ComponentName("com.android.tv.settings", "com.android.tv.settings.connectivity.NetworkActivity")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                },
                Intent().apply {
                    component = ComponentName("com.android.tv.settings", "com.android.tv.settings.MainSettings")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                },
                Intent(Settings.ACTION_WIFI_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                },
                Intent(Settings.ACTION_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )

            for (intent in intentList) {
                try {
                    context.startActivity(intent)
                    return
                } catch (e: Exception) {
                    // Try next
                }
            }
        }

        fun openGeneralSettings(context: Context) {
            val intentList = listOf(
                Intent().apply {
                    component = ComponentName("com.android.tv.settings", "com.android.tv.settings.MainSettings")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                },
                Intent(Settings.ACTION_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )

            for (intent in intentList) {
                try {
                    context.startActivity(intent)
                    return
                } catch (e: Exception) {
                    // Try next
                }
            }
        }

        fun openDefaultHomeSettings(context: Context) {
            val intentList = listOf(
                Intent(Settings.ACTION_HOME_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                },
                Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                },
                Intent().apply {
                    component = ComponentName("com.android.tv.settings", "com.android.tv.settings.MainSettings")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                },
                Intent(Settings.ACTION_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )

            for (intent in intentList) {
                try {
                    context.startActivity(intent)
                    return
                } catch (e: Exception) {
                    // Try next
                }
            }
        }

        fun openAccessibilitySettings(context: Context) {
            val intentList = listOf(
                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                },
                Intent().apply {
                    component = ComponentName("com.android.tv.settings", "com.android.tv.settings.system.AccessibilityActivity")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                },
                Intent(Settings.ACTION_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )

            for (intent in intentList) {
                try {
                    context.startActivity(intent)
                    return
                } catch (e: Exception) {
                    // Try next
                }
            }
        }
    }
}
