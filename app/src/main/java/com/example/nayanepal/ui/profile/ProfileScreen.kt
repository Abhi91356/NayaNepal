package com.example.nayanepal.ui.profile

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
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
import com.example.nayanepal.core.auth.domain.model.User
import com.example.nayanepal.core.designsystem.theme.SpatialIcons

@Composable
fun ProfileScreen(
    user: User?,
    onEditProfile: () -> Unit,
    onSecurityClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF003893), Color(0xFF111827))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            PassportCard3D(user, onEditProfile)
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "ACCOUNT PRIVACY",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 2.sp,
                modifier = Modifier.fillMaxWidth().padding(start = 8.dp, bottom = 20.dp)
            )
            
            ProfileBentoItem(
                title = "Security & Privacy",
                subtitle = "Manage biometric keys",
                icon = { SpatialIcons.Security(tint = it) },
                color = Color(0xFFFACC15), // Gold
                onClick = onSecurityClick
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ProfileBentoItem(
                title = "Logout Session",
                subtitle = "Safely end access",
                icon = { SpatialIcons.Logout(tint = it) },
                color = Color(0xFFEF4444), // Red
                onClick = onLogoutClick,
                isDestructive = true
            )
            
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun PassportCard3D(user: User?, onEdit: () -> Unit) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "flip"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable { isFlipped = !isFlipped }
    ) {
        if (rotation <= 90f || rotation >= 270f) {
            // Passport Front
            Surface(
                modifier = Modifier.fillMaxSize().shadow(24.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier.size(80.dp).shadow(8.dp, CircleShape),
                        shape = CircleShape,
                        color = Color(0xFFF3F4F6)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            SpatialIcons.Profile(tint = Color(0xFFDC143C))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = user?.name ?: "Abhishek Kumar Sah",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF111827)
                    )
                    
                    Text(
                        text = user?.email ?: "user@example.com",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    
                    TextButton(onClick = onEdit, modifier = Modifier.padding(top = 12.dp)) {
                        Text("UPDATE DETAILS", fontWeight = FontWeight.Black, color = Color(0xFF003893), letterSpacing = 1.sp)
                    }
                }
            }
        } else {
            // Passport Back (Verification Stats)
            Surface(
                modifier = Modifier.fillMaxSize().graphicsLayer { rotationY = 180f }.shadow(24.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                color = Color(0xFF003893)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(28.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("DIGITAL CITIZEN ID", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Status: VERIFIED", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Text("Identity Level: 2", color = Color(0xFF4ADE80), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(32.dp))
                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "SECURE BLOCKCHAIN TOKEN: 8XJ-92L",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileBentoItem(
    title: String,
    subtitle: String,
    icon: @Composable (Color) -> Unit,
    color: Color,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")
    
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(if (isPressed) 4.dp else 16.dp, RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp).shadow(6.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                color = color.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    icon(color)
                }
            }
            
            Column(modifier = Modifier.padding(start = 20.dp).weight(1f)) {
                Text(text = title, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = if (isDestructive) Color(0xFFEF4444) else Color(0xFF111827))
                Text(text = subtitle, fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            }
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
