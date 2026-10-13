package com.example

import com.example.data.LauncherConfig
import com.example.data.defaultDockApps
import com.example.ui.theme.DockThemes
import com.example.ui.theme.LauncherThemes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LauncherConfigTest {

    @Test
    fun testDefaultConfigValues() {
        val config = LauncherConfig()
        assertEquals("LOGO CUSTOMIZADA", config.logoText)
        assertEquals("177.153.68.5", config.vpsHost)
        assertEquals("/var/www/painel", config.vpsPath)
        assertEquals(5, config.dockApps.size)
        assertEquals("UniTV", config.dockApps[0].name)
        assertEquals("Google Play", config.dockApps[1].name)
        assertEquals("Prime Video", config.dockApps[2].name)
        assertEquals("Netflix", config.dockApps[3].name)
        assertEquals("YouTube TV", config.dockApps[4].name)
    }

    @Test
    fun testThemeOptionsCount() {
        assertEquals("Deve ter exatamente 6 temas de fundo para o launcher", 6, LauncherThemes.size)
        assertEquals("Deve ter exatamente 6 temas harmônicos para a dock", 6, DockThemes.size)
    }
}
