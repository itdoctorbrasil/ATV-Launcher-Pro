package com.example.ui.saas

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.ViewCompact
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DockAppItem
import com.example.data.LauncherConfig
import com.example.data.ManagedDevice
import com.example.ui.components.dpadFocusable

enum class SaasSidebarTab(val label: String, val icon: ImageVector) {
    VISAO_GERAL("Visão Geral", Icons.Default.GridView),
    GERENCIAR_CLIENTES("Gerenciar Clientes", Icons.Default.People),
    PLANO_FUNDO("Plano de fundo", Icons.Default.Image),
    GRADE_APPS("Grade de Apps", Icons.Default.ViewCompact),
    VIDEO_PUBLICIDADE("Video de publicidade", Icons.Default.Videocam),
    QR_CODE("QR Code WhatsApp", Icons.Default.QrCode2),
    LOGO_CUSTOMIZADA("Logo Customizada", Icons.Default.Image),
    BAIXAR_LAUNCHER("Baixar Launcher", Icons.Default.Code)
}

@Composable
fun SaaSPaneScreen(
    currentConfig: LauncherConfig,
    onUpdateLogo: (String, String) -> Unit,
    onUpdateVideoUrl: (String) -> Unit,
    onUpdateOta: (String, String, String) -> Unit,
    onUpdateDockApp: (Int, DockAppItem) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onClose)

    var isDarkTheme by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(SaasSidebarTab.VISAO_GERAL) }
    var isAuthenticated by remember { mutableStateOf(true) } // Ready for immediate view

    // Colors matching the screenshots
    val sidebarBg = Color(0xFF070B19)
    val sidebarBorder = Color(0xFF1E2640)
    val sidebarTextInactive = Color(0xFF94A3B8)
    val primaryPurple = Color(0xFF7C3AED)
    val purpleGradient = Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9)))

    val mainBg = if (isDarkTheme) Color(0xFF0B0F1F) else Color(0xFFF0F4FA)
    val cardBg = if (isDarkTheme) Color(0xFF131A2E) else Color.White
    val textDark = if (isDarkTheme) Color(0xFFF8FAFC) else Color(0xFF1E293B)
    val textMuted = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
    val borderLight = if (isDarkTheme) Color(0xFF242E49) else Color(0xFFE2E8F0)

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(mainBg)
    ) {
        // ================= 1. SIDEBAR LATERAL (Esquerda) =================
        Column(
            modifier = Modifier
                .width(260.dp)
                .fillMaxHeight()
                .background(sidebarBg)
                .border(width = 1.dp, color = sidebarBorder)
                .padding(vertical = 18.dp, horizontal = 14.dp)
        ) {
            // Header: "L" Badge + ATV LAUNCHER + Painel de Revendedor
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF6D28D9)))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "L",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Column {
                    Row {
                        Text("ATV ", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("LAUNCHER", color = Color(0xFF818CF8), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("Painel de Revendedor", color = Color(0xFF64748B), fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Box Conta Revenda
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, sidebarBorder, RoundedCornerShape(12.dp))
                    .background(Color(0xFF12182D).copy(alpha = 0.7f))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "CONTA REVENDA",
                                color = Color(0xFF94A3B8),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("Usuário: Login teste", color = Color(0xFFF8FAFC), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("E-mail: login.teste@gmail.com", color = Color(0xFF64748B), fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 8 Menus Verticais
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SaasSidebarTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .dpadFocusable(
                                onClick = { selectedTab = tab },
                                shape = RoundedCornerShape(12.dp),
                                focusedBorderColor = Color.White
                            )
                            .background(if (isSelected) purpleGradient else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)))
                            .padding(horizontal = 14.dp, vertical = 9.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else sidebarTextInactive,
                                modifier = Modifier.size(17.dp)
                            )
                            Text(
                                text = tab.label,
                                color = if (isSelected) Color.White else sidebarTextInactive,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Voltar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .dpadFocusable(onClick = onClose, shape = RoundedCornerShape(10.dp))
                    .border(1.dp, sidebarBorder, RoundedCornerShape(10.dp))
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Voltar ao Launcher", color = Color(0xFF94A3B8), fontSize = 12.sp)
            }
        }

        // ================= 2. ÁREA DE CONTEÚDO (Direita) =================
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = when (selectedTab) {
                                SaasSidebarTab.VISAO_GERAL -> "Painel de Controle da Revenda"
                                SaasSidebarTab.GERENCIAR_CLIENTES -> "Painel de Controle da Revenda"
                                SaasSidebarTab.PLANO_FUNDO -> "Plano de Fundo do Launcher"
                                SaasSidebarTab.GRADE_APPS -> "Grade de Apps da Dock"
                                SaasSidebarTab.VIDEO_PUBLICIDADE -> "Vídeo de Publicidade"
                                SaasSidebarTab.QR_CODE -> "QR Code WhatsApp de Suporte"
                                SaasSidebarTab.LOGO_CUSTOMIZADA -> "Logo Customizada"
                                SaasSidebarTab.BAIXAR_LAUNCHER -> "Baixar ATV Launcher"
                            },
                            color = textDark,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Orange Pill: "Login teste"
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFFFDBA74), RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFF7ED))
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Text("Login teste", color = Color(0xFFEA580C), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text(
                        text = "Acompanhe o desempenho e a gestão dos seus clientes finais",
                        color = textMuted,
                        fontSize = 12.sp
                    )
                }

                // Theme Toggle
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, borderLight, RoundedCornerShape(10.dp))
                        .background(cardBg)
                        .clickable { isDarkTheme = !isDarkTheme },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.WbSunny else Icons.Default.NightlightRound,
                        contentDescription = "Alternar Tema",
                        tint = textMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Body Area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                when (selectedTab) {
                    SaasSidebarTab.VISAO_GERAL -> VisaoGeralView(
                        cardBg = cardBg,
                        borderLight = borderLight,
                        textDark = textDark,
                        textMuted = textMuted,
                        onGoToClients = { selectedTab = SaasSidebarTab.GERENCIAR_CLIENTES }
                    )
                    SaasSidebarTab.GERENCIAR_CLIENTES -> GerenciarClientesView(
                        cardBg = cardBg,
                        borderLight = borderLight,
                        textDark = textDark,
                        textMuted = textMuted
                    )
                    SaasSidebarTab.PLANO_FUNDO -> PlanoFundoView(
                        cardBg = cardBg,
                        borderLight = borderLight,
                        textDark = textDark,
                        textMuted = textMuted
                    )
                    SaasSidebarTab.GRADE_APPS -> GradeAppsView(
                        cardBg = cardBg,
                        borderLight = borderLight,
                        textDark = textDark,
                        textMuted = textMuted,
                        config = currentConfig
                    )
                    SaasSidebarTab.VIDEO_PUBLICIDADE -> VideoPublicidadeView(
                        cardBg = cardBg,
                        borderLight = borderLight,
                        textDark = textDark,
                        textMuted = textMuted,
                        config = currentConfig,
                        onUpdateVideo = onUpdateVideoUrl
                    )
                    SaasSidebarTab.QR_CODE -> QrCodeView(
                        cardBg = cardBg,
                        borderLight = borderLight,
                        textDark = textDark,
                        textMuted = textMuted
                    )
                    SaasSidebarTab.LOGO_CUSTOMIZADA -> LogoCustomizadaView(
                        cardBg = cardBg,
                        borderLight = borderLight,
                        textDark = textDark,
                        textMuted = textMuted,
                        config = currentConfig,
                        onUpdateLogo = onUpdateLogo
                    )
                    SaasSidebarTab.BAIXAR_LAUNCHER -> BaixarLauncherView(
                        cardBg = cardBg,
                        borderLight = borderLight,
                        textDark = textDark,
                        textMuted = textMuted
                    )
                }
            }
        }
    }
}

// ================= VIEW 1: VISÃO GERAL (General.png) =================
@Composable
private fun VisaoGeralView(
    cardBg: Color,
    borderLight: Color,
    textDark: Color,
    textMuted: Color,
    onGoToClients: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderLight),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEDE9FE))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("PAINEL DE CONTROLE DA REVENDA", color = Color(0xFF6D28D9), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Visão Geral: Login teste", color = textDark, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Acompanhe abaixo o resumo dos seus dispositivos ativos, expirados e o status geral da sua revenda.", color = textMuted, fontSize = 12.sp)
                }

                Box(
                    modifier = Modifier
                        .dpadFocusable(onClick = onGoToClients, shape = RoundedCornerShape(10.dp))
                        .background(Color(0xFF4F46E5), RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 9.dp)
                ) {
                    Text("Gerenciar Meus Clientes", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3 Stat Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Card 1: Total
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderLight),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("TOTAL DE DISPOSITIVOS ", color = textDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = textDark, modifier = Modifier.size(11.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("2", color = textDark, fontSize = 32.sp, fontWeight = FontWeight.Black)
                        Text("TV Boxes e Smart TVs cadastradas", color = textMuted, fontSize = 11.sp)
                    }
                }

                // Card 2: Ativas
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderLight),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("ASSINATURAS ATIVAS ", color = Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(11.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("2", color = textDark, fontSize = 32.sp, fontWeight = FontWeight.Black)
                        Text("Dispositivos com acesso liberado", color = textMuted, fontSize = 11.sp)
                    }
                }

                // Card 3: Expirados
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderLight),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("EXPIRADOS / BLOQUEADOS ", color = Color(0xFFEF4444), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(11.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("0", color = textDark, fontSize = 32.sp, fontWeight = FontWeight.Black)
                        Text("Exigem renovação de plano", color = textMuted, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// ================= VIEW 2: GERENCIAR CLIENTES (DEVICES.png) =================
@Composable
private fun GerenciarClientesView(
    cardBg: Color,
    borderLight: Color,
    textDark: Color,
    textMuted: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderLight),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEDE9FE))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("PAINEL DE CONTROLE DA REVENDA", color = Color(0xFF6D28D9), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Clientes: Login teste", color = textDark, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Acompanhe abaixo o resumo dos seus dispositivos ativos, expirados e o status geral da sua revenda.", color = textMuted, fontSize = 12.sp)
                }

                Box(
                    modifier = Modifier
                        .dpadFocusable(onClick = {}, shape = RoundedCornerShape(10.dp))
                        .background(Color(0xFF7C3AED), RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 9.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Text("Cadastrar Novo Dispositivo", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Toolbar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search box
                Box(
                    modifier = Modifier
                        .width(340.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, borderLight, RoundedCornerShape(20.dp))
                        .background(Color(0xFFF8FAFC))
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = textMuted, modifier = Modifier.size(16.dp))
                        Text("Buscar cliente por Nome, MAC Address ou Token...", color = textMuted, fontSize = 11.sp)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(14.dp)).background(Color(0xFF4F46E5)).padding(horizontal = 12.dp, vertical = 5.dp)) {
                        Text("Todos", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(14.dp)).border(1.dp, borderLight, RoundedCornerShape(14.dp)).padding(horizontal = 12.dp, vertical = 5.dp)) {
                        Text("Ativos", color = textMuted, fontSize = 11.sp)
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(14.dp)).border(1.dp, borderLight, RoundedCornerShape(14.dp)).padding(horizontal = 12.dp, vertical = 5.dp)) {
                        Text("Expirados", color = textMuted, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Clients Table Headers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("CLIENTE / DISPOSITIVO", color = textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
                Text("MAC ADDRESS", color = textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
                Text("TOKEN DE ATIVAÇÃO", color = textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
                Text("VALIDADE", color = textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.9f))
                Text("STATUS", color = textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.8f))
                Text("AÇÕES", color = textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f))
            }

            // Row 1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("teste", color = textDark, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
                Text("84:D4:D2:33:D4:D8", color = Color(0xFF2563EB), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
                Text("84:D4:D2:33:D4:D8", color = Color(0xFFC2410C), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
                Box(modifier = Modifier.weight(0.9f)) {
                    Text("25/12/2026", color = textDark, fontSize = 11.sp, modifier = Modifier.border(1.dp, borderLight, RoundedCornerShape(8.dp)).padding(horizontal = 6.dp, vertical = 2.dp))
                }
                Box(modifier = Modifier.weight(0.8f)) {
                    Text("ATIVO", color = Color(0xFF16A34A), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.background(Color(0xFFDCFCE7), RoundedCornerShape(8.dp)).padding(horizontal = 6.dp, vertical = 2.dp))
                }
                Row(modifier = Modifier.weight(0.7f), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = textDark, modifier = Modifier.size(16.dp))
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE11D48), modifier = Modifier.size(16.dp))
                }
            }

            // Row 2
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1.2f)) {
                    Text("Rene rancho", color = textDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("\"Cobrar dia 10\"", color = textMuted, fontSize = 10.sp)
                }
                Text("A4:B0:39:93:BC:9C", color = Color(0xFF2563EB), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
                Text("A4:B0:39:93:BC:9C", color = Color(0xFFC2410C), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
                Box(modifier = Modifier.weight(0.9f)) {
                    Text("25/1/2027", color = textDark, fontSize = 11.sp, modifier = Modifier.border(1.dp, borderLight, RoundedCornerShape(8.dp)).padding(horizontal = 6.dp, vertical = 2.dp))
                }
                Box(modifier = Modifier.weight(0.8f)) {
                    Text("ATIVO", color = Color(0xFF16A34A), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.background(Color(0xFFDCFCE7), RoundedCornerShape(8.dp)).padding(horizontal = 6.dp, vertical = 2.dp))
                }
                Row(modifier = Modifier.weight(0.7f), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = textDark, modifier = Modifier.size(16.dp))
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE11D48), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// ================= VIEW 3: PLANO DE FUNDO =================
@Composable
private fun PlanoFundoView(
    cardBg: Color,
    borderLight: Color,
    textDark: Color,
    textMuted: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderLight),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text("Plano de Fundo do Launcher", color = textDark, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Escolha a cor de fundo e a atmosfera padrão dos clientes da sua revenda.", color = textMuted, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(18.dp))

            val themes = listOf("Violeta Profundo", "Azul Meia-Noite", "Preto Ônix (OLED)", "Verde Esmeralda", "Lavanda Suave", "Cinza Ardósia")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                themes.forEachIndexed { idx, name ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (idx == 0) Color(0xFF7C3AED) else Color(0xFF1E293B))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// ================= VIEW 4: GRADE DE APPS =================
@Composable
private fun GradeAppsView(
    cardBg: Color,
    borderLight: Color,
    textDark: Color,
    textMuted: Color,
    config: LauncherConfig
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderLight),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text("Grade de Apps Fixa na Dock", color = textDark, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Defina os 5 aplicativos de destaque exibidos na dock inferior da tela inicial.", color = textMuted, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(18.dp))

            config.dockApps.forEachIndexed { i, app ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("App ${i + 1}:", color = Color(0xFF7C3AED), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(app.name, color = textDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("(${app.packageName})", color = textMuted, fontSize = 11.sp)
                }
            }
        }
    }
}

// ================= VIEW 5: VIDEO PUBLICIDADE =================
@Composable
private fun VideoPublicidadeView(
    cardBg: Color,
    borderLight: Color,
    textDark: Color,
    textMuted: Color,
    config: LauncherConfig,
    onUpdateVideo: (String) -> Unit
) {
    var videoInput by remember { mutableStateOf(config.videoUrl) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderLight),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text("Vídeo de Publicidade em Looping", color = textDark, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Insira o link direto do vídeo institucional ou promocional em reprodução contínua.", color = textMuted, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = videoInput,
                onValueChange = { videoInput = it },
                label = { Text("URL do Vídeo (MP4 / HLS)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .dpadFocusable(onClick = { onUpdateVideo(videoInput) }, shape = RoundedCornerShape(10.dp))
                    .background(Color(0xFF7C3AED), RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 9.dp)
            ) {
                Text("Transmitir Vídeo para Clientes", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ================= VIEW 6: QR CODE =================
@Composable
private fun QrCodeView(
    cardBg: Color,
    borderLight: Color,
    textDark: Color,
    textMuted: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderLight),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text("QR Code WhatsApp para Suporte", color = textDark, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Configure o contato do WhatsApp exibido nas telas dos seus clientes.", color = textMuted, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(value = "5511987654321", onValueChange = {}, label = { Text("WhatsApp (com DDI/DDD)") }, modifier = Modifier.fillMaxWidth())
        }
    }
}

// ================= VIEW 7: LOGO CUSTOMIZADA (LOGO.png) =================
@Composable
private fun LogoCustomizadaView(
    cardBg: Color,
    borderLight: Color,
    textDark: Color,
    textMuted: Color,
    config: LauncherConfig,
    onUpdateLogo: (String, String) -> Unit
) {
    var logoTextState by remember { mutableStateOf(config.logoText) }
    var logoUrlState by remember { mutableStateOf(config.logoUrl) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderLight),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEDE9FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = Color(0xFF7C3AED), modifier = Modifier.size(20.dp))
                }
                Column {
                    Text("Logo Customizada", color = textDark, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Faça o upload da logo que aparecerá no launcher dos seus clientes.", color = textMuted, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Left Column: Upload / Text input
                Column(modifier = Modifier.weight(1.1f)) {
                    Text("Selecione a Imagem ou Texto da Logo", color = textDark, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = logoTextState,
                        onValueChange = { logoTextState = it },
                        label = { Text("Texto da Logo") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = logoUrlState,
                        onValueChange = { logoUrlState = it },
                        label = { Text("URL da Imagem da Logo (Opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .dpadFocusable(
                                onClick = { onUpdateLogo(logoTextState, logoUrlState) },
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(Color(0xFF131A2E), RoundedCornerShape(10.dp))
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Salvar e Enviar Logo", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Right Column: Preview
                Column(
                    modifier = Modifier
                        .weight(0.9f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, borderLight, RoundedCornerShape(14.dp))
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("PRÉ - VISUALIZAÇÃO", color = textMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .border(1.dp, borderLight, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(logoTextState.ifBlank { "KRUGER VENDAS EXPRESS" }, color = textDark, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("✓ Logo ativa no servidor", color = Color(0xFF10B981), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ================= VIEW 8: BAIXAR LAUNCHER =================
@Composable
private fun BaixarLauncherView(
    cardBg: Color,
    borderLight: Color,
    textDark: Color,
    textMuted: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderLight),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text("Baixar ATV Launcher", color = textDark, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Pacote APK oficial para instalação nas TV Boxes e Smart TVs.", color = textMuted, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF7C3AED))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("Baixar APK Direto (v1.0.1)", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEDE9FE))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("Código Downloader: 894215", color = Color(0xFF6D28D9), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
