package com.example.nayanepal.core.designsystem.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

/**
 * Custom modifiers for the Immersive 3D Spatial UI.
 */
fun Modifier.floating3D(
    tiltX: Float = 0f,
    tiltY: Float = 0f,
    elevation: Float = 10f
): Modifier = this.graphicsLayer {
    rotationX = tiltX
    rotationY = tiltY
    cameraDistance = 12f * density
    shadowElevation = elevation.dp.toPx()
}

fun Modifier.glassmorphic3D(): Modifier = this.graphicsLayer {
    clip = true
    shape = androidx.compose.foundation.shape.RoundedCornerShape(32.dp)
}

@Composable
fun Modifier.spatialParallax(): Modifier {
    var rotationX by remember { mutableStateOf(0f) }
    var rotationY by remember { mutableStateOf(0f) }
    
    val animatedRotationX by animateFloatAsState(targetValue = rotationX, animationSpec = tween(500), label = "x")
    val animatedRotationY by animateFloatAsState(targetValue = rotationY, animationSpec = tween(500), label = "y")

    return this.pointerInput(Unit) {
        detectTransformGestures { _, _, _, rotation ->
            // Simulating depth by small rotation based on interaction
        }
    }.graphicsLayer {
        this.rotationX = animatedRotationX
        this.rotationY = animatedRotationY
        cameraDistance = 12f * density
    }
}

