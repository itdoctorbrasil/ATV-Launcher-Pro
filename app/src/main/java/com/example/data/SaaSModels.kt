package com.example.data

data class ResellerAccount(
    val id: String,
    val username: String,
    val name: String,
    val role: String = "revendedor", // "admin" or "revendedor"
    val email: String = "",
    val phone: String = "",
    val devicesAllowed: Int = 50,
    val activeDevicesCount: Int = 12,
    val status: String = "Ativo", // "Ativo", "Bloqueado"
    val notes: String = "Revendedor autorizado"
)

data class ManagedDevice(
    val id: String,
    val deviceName: String,
    val macAddress: String,
    val ipAddress: String,
    val resellerUsername: String,
    val appVersion: String,
    val status: String, // "Online", "Offline", "Bloqueado"
    val lastSeen: String
)

data class OtaReleaseInfo(
    val version: String,
    val versionCode: Int,
    val downloadUrl: String,
    val releaseDate: String,
    val changelog: String,
    val isMandatory: Boolean
)
