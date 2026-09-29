package com.example.nayanepal.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nayanepal.core.designsystem.theme.SpatialIcons

@Composable
fun SpatialNavigationBar(
    currentDestinationId: Int,
    onNavigate: (Int) -> Unit
) {
    val homeId = com.example.nayanepal.core.designsystem.R.id.homeFragment
    val notificationsId = com.example.nayanepal.core.designsystem.R.id.notificationsFragment
    val profileId = com.example.nayanepal.core.designsystem.R.id.profileFragment

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .height(80.dp)
            .shadow(16.dp, RoundedCornerShape(40.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.9f), Color.White)
                ),
                shape = RoundedCornerShape(40.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                title = "Home",
                icon = { SpatialIcons.Home(tint = it) },
                isSelected = currentDestinationId == homeId,
                onClick = { onNavigate(homeId) }
            )
            NavItem(
                title = "Notifications",
                icon = { SpatialIcons.Notifications(tint = it) },
                isSelected = currentDestinationId == notificationsId,
                onClick = { onNavigate(notificationsId) }
            )
            NavItem(
                title = "Profile",
                icon = { SpatialIcons.Profile(tint = it) },
                isSelected = currentDestinationId == profileId,
                onClick = { onNavigate(profileId) }
            )
        }
    }
}

@Composable
private fun RowScope.NavItem(
    title: String,
    icon: @Composable (Color) -> Unit,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(if (isSelected) 1.2f else 1f, label = "scale")
    val alpha by animateFloatAsState(if (isSelected) 1f else 0.5f, label = "alpha")
    val color by animateColorAsState(if (isSelected) Color(0xFFDC143C) else Color.Gray, label = "color")

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer { this.alpha = alpha },
            contentAlignment = Alignment.Center
        ) {
            icon(color)
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
            color = color,
            modifier = Modifier.graphicsLayer { this.alpha = alpha }
        )
    }
}
