package com.example.ui.theme

import androidx.compose.ui.graphics.Color

data class LauncherThemeOption(
    val id: Int,
    val name: String,
    val backgroundColor: Color,
    val gradientEndColor: Color,
    val isDark: Boolean
)

data class DockThemeOption(
    val id: Int,
    val name: String,
    val backgroundColor: Color,
    val textColor: Color,
    val borderColor: Color
)

val LauncherThemes = listOf(
    LauncherThemeOption(
        id = 0,
        name = "Violeta Profundo",
        backgroundColor = Color(0xFF221A3B),
        gradientEndColor = Color(0xFF140F24),
        isDark = true
    ),
    LauncherThemeOption(
        id = 1,
        name = "Azul Meia-Noite",
        backgroundColor = Color(0xFF0F172A),
        gradientEndColor = Color(0xFF020617),
        isDark = true
    ),
    LauncherThemeOption(
        id = 2,
        name = "Preto Ônix (OLED)",
        backgroundColor = Color(0xFF121214),
        gradientEndColor = Color(0xFF050505),
        isDark = true
    ),
    LauncherThemeOption(
        id = 3,
        name = "Verde Esmeralda",
        backgroundColor = Color(0xFF0A332A),
        gradientEndColor = Color(0xFF031A14),
        isDark = true
    ),
    LauncherThemeOption(
        id = 4,
        name = "Lavanda Suave",
        backgroundColor = Color(0xFF4C3E6B),
        gradientEndColor = Color(0xFF2E2442),
        isDark = true
    ),
    LauncherThemeOption(
        id = 5,
        name = "Cinza Ardósia",
        backgroundColor = Color(0xFF243044),
        gradientEndColor = Color(0xFF111827),
        isDark = true
    )
)

val DockThemes = listOf(
    DockThemeOption(
        id = 0,
        name = "Branco Translúcido",
        backgroundColor = Color(0xEFFFFFFF),
        textColor = Color(0xFF1A1A1A),
        borderColor = Color(0x33FFFFFF)
    ),
    DockThemeOption(
        id = 1,
        name = "Fumê Escuro",
        backgroundColor = Color(0xD91E1933),
        textColor = Color(0xFFF3F4F6),
        borderColor = Color(0x4D8B5CF6)
    ),
    DockThemeOption(
        id = 2,
        name = "Lavanda Translúcido",
        backgroundColor = Color(0xF2EDE7F6),
        textColor = Color(0xFF2E1A47),
        borderColor = Color(0x66B39DDB)
    ),
    DockThemeOption(
        id = 3,
        name = "Dourado Champanhe",
        backgroundColor = Color(0xEBFFF8E7),
        textColor = Color(0xFF3E2723),
        borderColor = Color(0x66FFE082)
    ),
    DockThemeOption(
        id = 4,
        name = "Ciano Glacial",
        backgroundColor = Color(0xEBE0F2FE),
        textColor = Color(0xFF0C4A6E),
        borderColor = Color(0x667DD3FC)
    ),
    DockThemeOption(
        id = 5,
        name = "Prata Platina",
        backgroundColor = Color(0xF2F1F5F9),
        textColor = Color(0xFF1E293B),
        borderColor = Color(0x6694A3B8)
    )
)
