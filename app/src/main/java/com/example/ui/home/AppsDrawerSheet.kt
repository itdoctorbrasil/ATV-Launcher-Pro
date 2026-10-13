package com.example.ui.home

import android.graphics.drawable.Drawable
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.drawable.toBitmap
import com.example.data.InstalledAppItem
import com.example.ui.components.dpadFocusable

@Composable
fun AppsDrawerSheet(
    apps: List<InstalledAppItem>,
    isLoading: Boolean,
    onLaunchApp: (String) -> Unit,
    onUninstallApp: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onClose)

    var selectedAppForAction by remember { mutableStateOf<InstalledAppItem?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xF0120E22),
                        Color(0xF50C0917),
                        Color(0xFA080610)
                    )
                )
            )
            .padding(horizontal = 40.dp, vertical = 20.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header: Return prompt & Close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .dpadFocusable(
                                onClick = onClose,
                                shape = CircleShape,
                                focusedBorderColor = Color.White
                            )
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF281E48)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Voltar para Início",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Apps, contentDescription = null, tint = Color(0xFFA78BFA), modifier = Modifier.size(18.dp))
                            Text(
                                text = "Aplicativos Instalados",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Pressione OK para abrir ou gerenciar / desinstalar. Pressione Cima ou Voltar para fechar.",
                            color = Color(0xFF9E92B8),
                            fontSize = 12.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .dpadFocusable(
                            onClick = onClose,
                            shape = RoundedCornerShape(16.dp),
                            focusedBorderColor = Color.White
                        )
                        .border(1.dp, Color(0xFF3F355C), RoundedCornerShape(16.dp))
                        .background(Color(0xFF1B1430), RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Fechar Grade",
                        color = Color(0xFFD1D5DB),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFA78BFA))
                }
            } else if (apps.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhum aplicativo encontrado no sistema.",
                        color = Color(0xFFA199B8),
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 130.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(apps, key = { _, it -> it.packageName }) { index, app ->
                        AppGridItemCard(
                            app = app,
                            isTopRow = index < 6,
                            onNavigateUp = onClose,
                            onClick = {
                                selectedAppForAction = app
                            }
                        )
                    }
                }
            }
        }

        // App Action Dialog (Open or Uninstall)
        selectedAppForAction?.let { app ->
            AppActionDialog(
                app = app,
                onOpen = {
                    val pkg = app.packageName
                    selectedAppForAction = null
                    onLaunchApp(pkg)
                },
                onUninstall = {
                    val pkg = app.packageName
                    selectedAppForAction = null
                    onUninstallApp(pkg)
                },
                onDismiss = {
                    selectedAppForAction = null
                }
            )
        }
    }
}

@Composable
private fun AppGridItemCard(
    app: InstalledAppItem,
    isTopRow: Boolean = false,
    onNavigateUp: () -> Unit = {},
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.DirectionUp && isTopRow) {
                    onNavigateUp()
                    true
                } else {
                    false
                }
            }
            .dpadFocusable(
                onClick = onClick,
                shape = RoundedCornerShape(16.dp),
                focusedBorderColor = Color.White,
                focusedScale = 1.08f
            )
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFF2E2448), RoundedCornerShape(16.dp))
            .background(Color(0xFF1C1630).copy(alpha = 0.85f))
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF281F44)),
            contentAlignment = Alignment.Center
        ) {
            if (app.icon != null) {
                val bitmap = remember(app.icon) {
                    try {
                        app.icon.toBitmap(96, 96)
                    } catch (e: Exception) {
                        null
                    }
                }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = app.label,
                        modifier = Modifier.size(46.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Apps,
                        contentDescription = app.label,
                        tint = Color(0xFFA78BFA),
                        modifier = Modifier.size(32.dp)
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Apps,
                    contentDescription = app.label,
                    tint = Color(0xFFA78BFA),
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = app.label,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AppActionDialog(
    app: InstalledAppItem,
    onOpen: () -> Unit,
    onUninstall: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .width(420.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, Color(0xFF4C3D70), RoundedCornerShape(20.dp))
                .background(Color(0xFF161126))
                .padding(22.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = app.label,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = app.packageName,
                    color = Color(0xFF9E92B8),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action 1: Open
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .dpadFocusable(
                            onClick = onOpen,
                            shape = RoundedCornerShape(14.dp),
                            focusedBorderColor = Color.White
                        )
                        .background(Color(0xFF382962), RoundedCornerShape(14.dp))
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Text("Abrir Aplicativo", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action 2: Uninstall (Only if not system app)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .dpadFocusable(
                            onClick = onUninstall,
                            shape = RoundedCornerShape(14.dp),
                            focusedBorderColor = Color.White
                        )
                        .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(14.dp))
                        .background(Color(0xFF2C1420), RoundedCornerShape(14.dp))
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFF87171), modifier = Modifier.size(18.dp))
                        Text(
                            text = if (app.isSystemApp) "Desinstalar (App do Sistema)" else "Desinstalar Aplicativo",
                            color = Color(0xFFFCA5A5),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Cancel
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .dpadFocusable(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(14.dp),
                            focusedBorderColor = Color.White
                        )
                        .background(Color(0xFF211938), RoundedCornerShape(14.dp))
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Cancelar", color = Color(0xFFD1D5DB), fontSize = 13.sp)
                }
            }
        }
    }
}
