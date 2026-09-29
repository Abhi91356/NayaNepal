package com.example.nayanepal.core.auth.presentation.ui

import androidx.biometric.BiometricPrompt
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nayanepal.core.auth.presentation.state.LoginState
import com.example.nayanepal.core.auth.presentation.viewmodel.AuthViewModel
import com.example.nayanepal.core.designsystem.R as DesignR

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showBiometricDialog by remember { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()

    // 3D Floating Animation for Logo
    val infiniteTransition = rememberInfiniteTransition(label = "logoFloat")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "float"
    )
    val rotationAnim by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "rotation"
    )

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> onLoginSuccess((loginState as LoginState.Success).user.id)
            is LoginState.Error -> {
                snackbarHostState.showSnackbar((loginState as LoginState.Error).message)
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFDC143C), Color(0xFF003893))
                    )
                )
        ) {
            // Decorative Animated Background
            Box(
                modifier = Modifier
                    .size(350.dp)
                    .offset(x = (-50).dp, y = (-50).dp)
                    .graphicsLayer { rotationZ = rotationAnim * 2 }
                    .background(Color.White.copy(alpha = 0.08f), CircleShape)
                    .blur(60.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 3D Animated Logo
                Surface(
                    modifier = Modifier
                        .size(130.dp)
                        .graphicsLayer {
                            translationY = floatAnim
                            rotationY = rotationAnim * 3
                            cameraDistance = 8f * density
                        }
                        .shadow(30.dp, RoundedCornerShape(36.dp))
                        .border(2.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(36.dp)),
                    shape = RoundedCornerShape(36.dp),
                    color = Color.White
                ) {
                    Image(
                        painter = painterResource(id = DesignR.drawable.nepal_logo),
                        contentDescription = "Nepal Logo",
                        modifier = Modifier.padding(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "NayaNepal",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.graphicsLayer {
                        shadowElevation = 10.dp.toPx()
                        rotationX = floatAnim * 0.5f
                    }
                )

                Text(
                    text = "Secure Digital Gateway",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp, bottom = 48.dp)
                )

                // Glassmorphic 3D Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            rotationX = -floatAnim * 0.2f
                            cameraDistance = 10f * density
                        }
                        .clip(RoundedCornerShape(40.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(40.dp))
                        .padding(28.dp)
                ) {
                    Column {
                        Text(
                            text = "SIGN IN",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(18.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                cursorColor = Color.White
                            ),
                            leadingIcon = {
                                Icon(painterResource(DesignR.drawable.ic_email), null, tint = Color.White)
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            shape = RoundedCornerShape(18.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                cursorColor = Color.White
                            ),
                            leadingIcon = {
                                Icon(painterResource(DesignR.drawable.ic_lock), null, tint = Color.White)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        painter = painterResource(DesignR.drawable.visibility_off),
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = if (passwordVisible) 1f else 0.5f)
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                if (email.isNotEmpty() && password.isNotEmpty()) {
                                    viewModel.loginWithEmail(email, password)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .shadow(12.dp, RoundedCornerShape(30.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFFDC143C)
                            ),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            if (loginState is LoginState.Loading) {
                                CircularProgressIndicator(color = Color(0xFFDC143C), modifier = Modifier.size(28.dp))
                            } else {
                                Text("LOGIN TO PORTAL", fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Quick Access Biometrics
                Surface(
                    onClick = { showBiometricDialog = true },
                    modifier = Modifier
                        .size(64.dp)
                        .shadow(10.dp, CircleShape),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = DesignR.drawable.fingerprint),
                            contentDescription = "Biometric",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("New here?", color = Color.White.copy(alpha = 0.7f))
                    TextButton(onClick = onNavigateToRegister) {
                        Text("CREATE ACCOUNT", color = Color.White, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        if (showBiometricDialog) {
            BiometricLoginDialog(
                onAuthenticated = {
                    viewModel.completeBiometricLogin(it)
                    showBiometricDialog = false
                },
                onDismiss = { showBiometricDialog = false }
            )
        }
    }
}

@Composable
fun BiometricLoginDialog(
    onAuthenticated: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val executor = remember { ContextCompat.getMainExecutor(context) }
    
    val biometricPrompt = remember {
        BiometricPrompt(context as androidx.fragment.app.FragmentActivity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onAuthenticated("success")
                }
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onDismiss()
                }
            })
    }

    LaunchedEffect(Unit) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Identity Verification")
            .setSubtitle("Confirm your biometric to proceed")
            .setNegativeButtonText("Use Password")
            .build()
        
        biometricPrompt.authenticate(promptInfo)
    }
}
