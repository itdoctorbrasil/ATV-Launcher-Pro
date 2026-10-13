package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.dpadFocusable(
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(12.dp),
    focusedScale: Float = 1.06f,
    focusedBorderColor: Color = Color.White,
    borderWidth: Dp = 2.5.dp,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
): Modifier {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val animatedScale by animateFloatAsState(
        targetValue = if (isFocused) focusedScale else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "dpadScale"
    )

    return this
        .scale(animatedScale)
        .then(
            if (isFocused) {
                Modifier
                    .shadow(elevation = 12.dp, shape = shape, spotColor = focusedBorderColor)
                    .border(width = borderWidth, color = focusedBorderColor, shape = shape)
            } else {
                Modifier
            }
        )
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick
        )
        .focusable(enabled = enabled, interactionSource = interactionSource)
        .onKeyEvent { keyEvent ->
            if (keyEvent.key == Key.DirectionCenter || keyEvent.key == Key.Enter) {
                onClick()
                true
            } else {
                false
            }
        }
}
