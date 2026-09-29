package com.example.nayanepal.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * 3D Spatial Icon system.
 * High-clarity, oversized 3D icons for the Bento Grid.
 */
object SpatialIcons {

    @Composable
    fun Marketplace(modifier: Modifier = Modifier, tint: Color = Color.White) {
        SpatialIconContainer(modifier) {
            Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = "Marketplace",
                tint = tint,
                modifier = Modifier.size(32.dp) // Refined Size
            )
        }
    }

    @Composable
    fun Learning(modifier: Modifier = Modifier, tint: Color = Color.White) {
        SpatialIconContainer(modifier) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = "Learning",
                tint = tint,
                modifier = Modifier.size(32.dp) // Refined Size
            )
        }
    }

    @Composable
    fun Agriculture(modifier: Modifier = Modifier, tint: Color = Color.White) {
        SpatialIconContainer(modifier) {
            Icon(
                imageVector = Icons.Default.Grass,
                contentDescription = "Agriculture",
                tint = tint,
                modifier = Modifier.size(32.dp) // Refined Size
            )
        }
    }

    @Composable
    fun Health(modifier: Modifier = Modifier, tint: Color = Color.White) {
        SpatialIconContainer(modifier) {
            Icon(
                imageVector = Icons.Default.HealthAndSafety,
                contentDescription = "Health",
                tint = tint,
                modifier = Modifier.size(32.dp) // Refined Size
            )
        }
    }

    @Composable
    fun Tourism(modifier: Modifier = Modifier, tint: Color = Color.White) {
        SpatialIconContainer(modifier) {
            Icon(
                imageVector = Icons.Default.Landscape,
                contentDescription = "Tourism",
                tint = tint,
                modifier = Modifier.size(32.dp) // Refined Size
            )
        }
    }

    @Composable
    fun Accounting(modifier: Modifier = Modifier, tint: Color = Color.White) {
        SpatialIconContainer(modifier) {
            Icon(
                imageVector = Icons.Default.Calculate,
                contentDescription = "Accounting",
                tint = tint,
                modifier = Modifier.size(32.dp) // Refined Size
            )
        }
    }

    @Composable
    fun Home(modifier: Modifier = Modifier, tint: Color = Color.White) {
        SpatialIconContainer(modifier) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                tint = tint,
                modifier = Modifier.size(32.dp)
            )
        }
    }

    @Composable
    fun Notifications(modifier: Modifier = Modifier, tint: Color = Color.White) {
        SpatialIconContainer(modifier) {
            Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = "Notifications",
                tint = tint,
                modifier = Modifier.size(32.dp)
            )
        }
    }

    @Composable
    fun Profile(modifier: Modifier = Modifier, tint: Color = Color.White) {
        SpatialIconContainer(modifier) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = tint,
                modifier = Modifier.size(32.dp)
            )
        }
    }

    @Composable
    fun Security(modifier: Modifier = Modifier, tint: Color = Color.White) {
        SpatialIconContainer(modifier) {
            Icon(
                imageVector = Icons.Default.GppGood,
                contentDescription = "Security",
                tint = tint,
                modifier = Modifier.size(32.dp)
            )
        }
    }

    @Composable
    fun Logout(modifier: Modifier = Modifier, tint: Color = Color.White) {
        SpatialIconContainer(modifier) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Logout",
                tint = tint,
                modifier = Modifier.size(32.dp)
            )
        }
    }

    @Composable
    fun Edit(modifier: Modifier = Modifier, tint: Color = Color.White) {
        SpatialIconContainer(modifier) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    @Composable
    private fun SpatialIconContainer(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        Box(
            modifier = modifier
                .graphicsLayer {
                    // Small rotation only for depth, keeps it sharp
                    rotationX = 2f
                    rotationY = -2f
                    cameraDistance = 8f * density
                },
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}
