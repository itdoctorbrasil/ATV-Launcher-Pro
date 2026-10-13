package com.example.ui.components

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoPlayerView(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isVideoReady by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (!hasError && videoUrl.isNotBlank()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    SurfaceView(ctx).apply {
                        holder.addCallback(object : SurfaceHolder.Callback {
                            private var mediaPlayer: MediaPlayer? = null

                            override fun surfaceCreated(holder: SurfaceHolder) {
                                try {
                                    mediaPlayer = MediaPlayer().apply {
                                        setDisplay(holder)
                                        setDataSource(ctx, Uri.parse(videoUrl))
                                        setAudioAttributes(
                                            AudioAttributes.Builder()
                                                .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                                .build()
                                        )
                                        isLooping = true
                                        // Mute video on launcher preview to avoid annoying user with blaring audio
                                        setVolume(0f, 0f)
                                        setOnPreparedListener { mp ->
                                            isVideoReady = true
                                            mp.start()
                                        }
                                        setOnErrorListener { _, _, _ ->
                                            hasError = true
                                            false
                                        }
                                        prepareAsync()
                                    }
                                } catch (e: Exception) {
                                    hasError = true
                                }
                            }

                            override fun surfaceChanged(
                                holder: SurfaceHolder,
                                format: Int,
                                width: Int,
                                height: Int
                            ) {}

                            override fun surfaceDestroyed(holder: SurfaceHolder) {
                                try {
                                    mediaPlayer?.stop()
                                    mediaPlayer?.release()
                                } catch (e: Exception) {}
                                mediaPlayer = null
                            }
                        })
                    }
                }
            )
        }

        // Animated video fallback if loading or offline or no video set
        if (!isVideoReady || hasError || videoUrl.isBlank()) {
            VideoBannerFallback()
        }
    }
}

@Composable
fun VideoBannerFallback(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "bannerAnimation")
    val waveOffset by transition.animateFloat(
        initialValue = -100f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveOffset"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF2C3E50),
                        Color(0xFF3498DB),
                        Color(0xFF2C3E50)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw cinematic graphic wave lines
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 160f,
                center = Offset(size.width * 0.3f + waveOffset * 0.2f, size.height * 0.5f)
            )
            drawCircle(
                color = Color(0xFF00E676).copy(alpha = 0.25f),
                radius = 120f,
                center = Offset(size.width * 0.7f - waveOffset * 0.15f, size.height * 0.5f)
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Color.Black.copy(alpha = 0.45f))
                .padding(horizontal = 24.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.layout.Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Video Looping",
                    tint = Color.White,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "ATV STREAM PLAYER • TRANSMISSÃO CONTÍNUA",
                    color = Color.White,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
