package com.example.nayanepal.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nayanepal.core.designsystem.R as DesignR
import com.example.nayanepal.core.designsystem.theme.SpatialIcons
import com.example.nayanepal.core.designsystem.theme.floating3D
import java.util.Locale

@Composable
fun HomeScreen(
    onFeatureClick: (Int) -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF003893), Color(0xFF111827)) // Deep Professional Navy
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderSection(uiState, onProfileClick, onNotificationsClick)
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "E-SERVICES",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White.copy(alpha = 0.5f),
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // Balanced 2-column Grid for all items
                items(listOf(
                    FeatureItem("Marketplace", { SpatialIcons.Marketplace(tint = it) }, Color(0xFF2563EB)),
                    FeatureItem("Learning", { SpatialIcons.Learning(tint = it) }, Color(0xFF9333EA)),
                    FeatureItem("Agriculture", { SpatialIcons.Agriculture(tint = it) }, Color(0xFF16A34A)),
                    FeatureItem("Health", { SpatialIcons.Health(tint = it) }, Color(0xFFEA580C)),
                    FeatureItem("Tourism", { SpatialIcons.Tourism(tint = it) }, Color(0xFFDB2777)),
                    FeatureItem("Accounting", { SpatialIcons.Accounting(tint = it) }, Color(0xFF0D9488))
                )) { item ->
                    BentoCard(
                        title = item.title,
                        subtitle = "",
                        icon = { item.icon(it) },
                        color = item.bgColor,
                        isLarge = false,
                        onClick = { onFeatureClick(0) }
                    )
                }

                item(span = { GridItemSpan(2) }) {
                    NewsSectionBento()
                }
            }
        }
    }
}

@Composable
fun BentoCard(
    title: String,
    subtitle: String,
    icon: @Composable (Color) -> Unit,
    color: Color,
    isLarge: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.97f else 1f, label = "scale")
    val shadow by animateDpAsState(if (isPressed) 2.dp else 12.dp, label = "shadow")

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isLarge) 150.dp else 125.dp) // More compact height
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(shadow, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = if (isLarge) Alignment.Start else Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isLarge) 56.dp else 48.dp) // Smaller bubble
                        .background(color.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    icon(color)
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = title,
                    fontSize = if (isLarge) 18.sp else 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF111827)
                )
                
                if (isLarge && subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderSection(
    uiState: HomeUiState,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    val name = (uiState as? HomeUiState.Success)?.user?.name ?: "Abhishek Kumar Sah"
    val balance = 25500.00

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFDC143C), Color(0xFF003893))
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onProfileClick,
                    modifier = Modifier.size(44.dp).shadow(6.dp, CircleShape),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        SpatialIcons.Profile(tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                }

                Surface(
                    onClick = onNotificationsClick,
                    modifier = Modifier.size(44.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        SpatialIcons.Notifications(tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Namaste, $name",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Premium Glass Wallet
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .shadow(10.dp, RoundedCornerShape(20.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("TOTAL BALANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.6f), letterSpacing = 1.sp)
                        Text("रू ${String.format(Locale.US, "%.2f", balance)}", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("PAY", color = Color(0xFF003893), fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsSectionBento() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .shadow(10.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(DesignR.drawable.ic_pattern_mandala),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().alpha(0.04f),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(20.dp).align(Alignment.CenterStart)) {
                Surface(color = Color(0xFFDC143C), shape = RoundedCornerShape(8.dp)) {
                    Text("LIVE", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Secure Digital ID", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFF111827))
                Text("Verified with blockchain tech.", fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

data class FeatureItem(
    val title: String,
    val icon: @Composable (Color) -> Unit,
    val bgColor: Color
)
