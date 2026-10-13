package com.example.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.NetworkMonitor
import com.example.ui.components.dpadFocusable

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentStep by remember { mutableIntStateOf(0) } // 0, 1, 2, 3
    var showAccessibilityTermsModal by remember { mutableStateOf(false) }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E1735),
            Color(0xFF120E22),
            Color(0xFF0C0917)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(horizontal = 48.dp, vertical = 24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar: Step Indicator (center) + "Pular configuração" (right)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.width(130.dp))

                // 4 Progress pills
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0..3) {
                        val isActive = i == currentStep
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(if (isActive) 38.dp else 18.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (isActive) Color(0xFFA78BFA) else Color(0xFF3B2E58)
                                )
                        )
                    }
                }

                // "Pular configuração" button
                Box(
                    modifier = Modifier
                        .dpadFocusable(
                            onClick = onFinishOnboarding,
                            shape = RoundedCornerShape(20.dp),
                            focusedBorderColor = Color(0xFFA78BFA)
                        )
                        .border(1.dp, Color(0xFF3F355C), RoundedCornerShape(20.dp))
                        .background(Color(0xFF1E1736).copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Pular configuração",
                        color = Color(0xFFD1D5DB),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Main Content: 2-column split (Left: Text & Actions, Right: Preview Card)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                // Left Column: Interactive info & D-Pad buttons
                Column(
                    modifier = Modifier
                        .weight(1.1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    when (currentStep) {
                        0 -> StepOneContent(
                            onNext = { currentStep = 1 }
                        )
                        1 -> StepTwoContent(
                            onOpenSettings = {
                                NetworkMonitor.openDefaultHomeSettings(context)
                                currentStep = 2
                            },
                            onLater = { currentStep = 2 }
                        )
                        2 -> StepThreeContent(
                            onOpenAccessibility = { showAccessibilityTermsModal = true },
                            onLater = { currentStep = 3 }
                        )
                        3 -> StepFourContent(
                            onFinish = onFinishOnboarding
                        )
                    }
                }

                // Right Column: Preview mockup matching the screenshots
                Box(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    when (currentStep) {
                        0 -> MockupWelcomePreview()
                        1 -> MockupDefaultLauncherPreview()
                        2 -> MockupAccessibilityPreview()
                        3 -> MockupFinishPreview()
                    }
                }
            }
        }

        // Accessibility Terms Modal (ACESSIBILITY_TERMS.png)
        if (showAccessibilityTermsModal) {
            AccessibilityTermsDialog(
                onAccept = {
                    showAccessibilityTermsModal = false
                    NetworkMonitor.openAccessibilitySettings(context)
                    currentStep = 3
                },
                onDismiss = {
                    showAccessibilityTermsModal = false
                }
            )
        }
    }
}

// ================= STEP 1: WELCOME.png =================
@Composable
private fun StepOneContent(onNext: () -> Unit) {
    Text(
        text = "Bem-vindo ao ATV Launcher",
        color = Color.White,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 38.sp
    )

    Spacer(modifier = Modifier.height(14.dp))

    Text(
        text = "Um novo launcher para o seu Android TV.\nTrês passos curtos e a tela é sua.",
        color = Color(0xFFA199B8),
        fontSize = 16.sp,
        lineHeight = 22.sp
    )

    Spacer(modifier = Modifier.height(28.dp))

    Box(
        modifier = Modifier
            .dpadFocusable(
                onClick = onNext,
                shape = RoundedCornerShape(24.dp),
                focusedBorderColor = Color.White
            )
            .border(1.5.dp, Color(0xFFA78BFA), RoundedCornerShape(24.dp))
            .background(Color(0xFF281E48), RoundedCornerShape(24.dp))
            .padding(horizontal = 26.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Vamos lá",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// ================= STEP 2: DEFAULT_LAUNCHER.png =================
@Composable
private fun StepTwoContent(
    onOpenSettings: () -> Unit,
    onLater: () -> Unit
) {
    Text(
        text = "Definir launcher padrão.",
        color = Color.White,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "Defina o ATV launcher como padrão para que os botões Home e Voltar não te levem de volta ao launcher do Google.",
        color = Color(0xFFA199B8),
        fontSize = 15.sp,
        lineHeight = 22.sp
    )

    Spacer(modifier = Modifier.height(18.dp))

    // Info card
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF3B2E58), RoundedCornerShape(12.dp))
            .background(Color(0xFF1B1430).copy(alpha = 0.7f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = Color(0xFFA78BFA),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = "Se não funcionar no seu modelo de TV, você pode tentar mais tarde pelo Launcher Manager.",
            color = Color(0xFFB3ABC8),
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(
            modifier = Modifier
                .dpadFocusable(
                    onClick = onOpenSettings,
                    shape = RoundedCornerShape(24.dp),
                    focusedBorderColor = Color.White
                )
                .border(1.5.dp, Color(0xFFA78BFA), RoundedCornerShape(24.dp))
                .background(Color(0xFF281E48), RoundedCornerShape(24.dp))
                .padding(horizontal = 22.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Abrir configurações",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .dpadFocusable(
                    onClick = onLater,
                    shape = RoundedCornerShape(24.dp),
                    focusedBorderColor = Color.White
                )
                .border(1.dp, Color(0xFF3F355C), RoundedCornerShape(24.dp))
                .background(Color(0xFF1A142E), RoundedCornerShape(24.dp))
                .padding(horizontal = 22.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Mais tarde",
                color = Color(0xFFD1D5DB),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ================= STEP 3: ACESSIBILITY.png =================
@Composable
private fun StepThreeContent(
    onOpenAccessibility: () -> Unit,
    onLater: () -> Unit
) {
    Text(
        text = "Ativar acessibilidade.",
        color = Color.White,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "ATV usa um serviço de acessibilidade opcional para manter seu launcher confiável após Home, o launcher da TV e o despertar.",
        color = Color(0xFFA199B8),
        fontSize = 15.sp,
        lineHeight = 22.sp
    )

    Spacer(modifier = Modifier.height(18.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF3B2E58), RoundedCornerShape(12.dp))
            .background(Color(0xFF1B1430).copy(alpha = 0.7f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = Color(0xFFA78BFA),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = "Antes de abrir as configurações do Android, Monet mostra exatamente quais dados do serviço são usados.",
            color = Color(0xFFB3ABC8),
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(
            modifier = Modifier
                .dpadFocusable(
                    onClick = onOpenAccessibility,
                    shape = RoundedCornerShape(24.dp),
                    focusedBorderColor = Color.White
                )
                .border(1.5.dp, Color(0xFFA78BFA), RoundedCornerShape(24.dp))
                .background(Color(0xFF281E48), RoundedCornerShape(24.dp))
                .padding(horizontal = 22.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Abrir acessibilidade",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .dpadFocusable(
                    onClick = onLater,
                    shape = RoundedCornerShape(24.dp),
                    focusedBorderColor = Color.White
                )
                .border(1.dp, Color(0xFF3F355C), RoundedCornerShape(24.dp))
                .background(Color(0xFF1A142E), RoundedCornerShape(24.dp))
                .padding(horizontal = 22.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Mais tarde",
                color = Color(0xFFD1D5DB),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ================= STEP 4: FINISH.png =================
@Composable
private fun StepFourContent(onFinish: () -> Unit) {
    Text(
        text = "ATV está configurado.",
        color = Color.White,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Divirta-se!",
        color = Color(0xFFA199B8),
        fontSize = 16.sp
    )

    Spacer(modifier = Modifier.height(28.dp))

    Box(
        modifier = Modifier
            .dpadFocusable(
                onClick = onFinish,
                shape = RoundedCornerShape(24.dp),
                focusedBorderColor = Color.White
            )
            .border(1.5.dp, Color(0xFFA78BFA), RoundedCornerShape(24.dp))
            .background(Color(0xFF281E48), RoundedCornerShape(24.dp))
            .padding(horizontal = 28.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Para a tela inicial",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ================= DIALOG: ACESSIBILITY_TERMS.png =================
@Composable
private fun AccessibilityTermsDialog(
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .width(580.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, Color(0xFF4C3D70), RoundedCornerShape(24.dp))
                .background(Color(0xFF140F22))
                .padding(26.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF362858)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PriorityHigh,
                            contentDescription = null,
                            tint = Color(0xFFA78BFA),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "Serviço de acessibilidade",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "ATV é um launcher para Android TV, não uma ferramenta de acessibilidade para apoiar pessoas com deficiência. Este serviço opcional é usado apenas para o comportamento do launcher: detecta eventos da tecla Home, nomes de janelas do launcher/sistema em primeiro plano e eventos de despertar do modo de espera, para manter o ATV em primeiro plano após Home, o launcher da TV e o despertar.",
                    color = Color(0xFFC7BEDE),
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ATV não pode ler texto da tela nem conteúdo de janelas. Os dados do serviço são processados apenas no seu dispositivo e não são armazenados, enviados, vendidos, compartilhados, usados para anúncios ou para controlar outros apps.",
                    color = Color(0xFF9E92B8),
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .dpadFocusable(
                                onClick = onAccept,
                                shape = RoundedCornerShape(24.dp),
                                focusedBorderColor = Color.White
                            )
                            .border(1.5.dp, Color(0xFFA78BFA), RoundedCornerShape(24.dp))
                            .background(Color(0xFF2C1E54), RoundedCornerShape(24.dp))
                            .padding(horizontal = 20.dp, vertical = 11.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Aceitar e abrir configurações",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .dpadFocusable(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(24.dp),
                                focusedBorderColor = Color.White
                            )
                            .border(1.dp, Color(0xFF3F355C), RoundedCornerShape(24.dp))
                            .background(Color(0xFF1B1430), RoundedCornerShape(24.dp))
                            .padding(horizontal = 20.dp, vertical = 11.dp)
                    ) {
                        Text(
                            text = "Agora não",
                            color = Color(0xFFD1D5DB),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// ================= PREVIEWS / MOCKUPS =================

@Composable
private fun MockupWelcomePreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(230.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(2.dp, Color(0xFF3F325E), RoundedCornerShape(18.dp))
            .background(Color(0xFF5B4A82))
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Mockup top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(30.dp))
                Text("12:01", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Wifi, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                    Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                }
            }

            // Mockup Center video strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFE5A93B), Color(0xFF4CAF50), Color(0xFF42A5F5))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }

            // Mockup Dock at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("UniTV", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("Play", color = Color(0xFF2E7D32), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("Prime", color = Color(0xFF0D47A1), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("NETFLIX", color = Color(0xFFD32F2F), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("YouTube", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MockupDefaultLauncherPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, Color(0xFF382A52), RoundedCornerShape(20.dp))
            .background(Color(0xFF0F0B1A))
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2E2448)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFFA78BFA), modifier = Modifier.size(18.dp))
                }
                Column {
                    Text("Default home app", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("Android", color = Color(0xFF9E92B8), fontSize = 11.sp)
                }
            }

            // Google TV item
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1A132E))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Google TV", color = Color(0xFFD1D5DB), fontSize = 12.sp)
                    Text("–", color = Color(0xFF9E92B8), fontSize = 14.sp)
                }
            }

            // ATV Launcher item (Selected)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.5.dp, Color(0xFFA78BFA), RoundedCornerShape(10.dp))
                    .background(Color(0xFF523D8A))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFF43F5E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                        }
                        Text("ATV Launcher", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
private fun MockupAccessibilityPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, Color(0xFF382A52), RoundedCornerShape(20.dp))
            .background(Color(0xFF0F0B1A))
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2E2448)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Accessibility, contentDescription = null, tint = Color(0xFFA78BFA), modifier = Modifier.size(18.dp))
                }
                Text("Accessibility", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            // Audio description row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1A132E))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Audio description", color = Color(0xFFD1D5DB), fontSize = 12.sp)
                    Text("Aus", color = Color(0xFF9E92B8), fontSize = 12.sp)
                }
            }

            // ATV Launcher row with active toggle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.5.dp, Color(0xFFA78BFA), RoundedCornerShape(10.dp))
                    .background(Color(0xFF523D8A))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFF43F5E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
                        }
                        Text("ATV Launcher", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    // Switch pill indicator
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .padding(end = 2.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF523D8A))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MockupFinishPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, Color(0xFF382A52), RoundedCornerShape(20.dp))
            .background(Color(0xFF0F0B1A)),
        contentAlignment = Alignment.Center
    ) {
        // Concentric circles
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .border(1.dp, Color(0xFF2C2248), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, Color(0xFF4C3A7A), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFA78BFA)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF1E1736),
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
        }
    }
}
