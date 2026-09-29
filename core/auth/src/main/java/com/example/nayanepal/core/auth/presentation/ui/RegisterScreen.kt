package com.example.nayanepal.core.auth.presentation.ui

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nayanepal.core.auth.presentation.state.RegisterState
import com.example.nayanepal.core.auth.presentation.viewmodel.AuthViewModel
import com.example.nayanepal.core.designsystem.R as DesignR

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val registerState by viewModel.registerState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterState.Success -> onRegisterSuccess()
            is RegisterState.Error -> {
                snackbarHostState.showSnackbar((registerState as RegisterState.Error).message)
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
                        colors = listOf(
                            Color(0xFF003893), // Royal Blue
                            Color(0xFFDC143C)  // Crimson Red
                        )
                    )
                )
        ) {
            // Decorative background elements
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .offset(x = 200.dp, y = (-50).dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
                    .blur(40.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                // Back Button
                Row(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onNavigateToLogin,
                        modifier = Modifier
                            .shadow(4.dp, CircleShape)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = DesignR.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Create Account",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Text(
                    text = "Join the NayaNepal digital revolution",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 32.dp),
                    textAlign = TextAlign.Start
                )

                // Registration Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(32.dp))
                        .padding(24.dp)
                ) {
                    Column {
                        // Name Input
                        RegisterTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Full Name",
                            icon = DesignR.drawable.ic_person_avatar
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Email Input
                        RegisterTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email Address",
                            icon = DesignR.drawable.ic_email,
                            keyboardType = KeyboardType.Email
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Phone Input
                        RegisterTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = "Phone Number",
                            icon = DesignR.drawable.ic_refresh, // Using refresh as placeholder if phone icon missing
                            keyboardType = KeyboardType.Phone
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Password Input
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                cursorColor = Color.White,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                            ),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = DesignR.drawable.ic_lock),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        painter = painterResource(id = DesignR.drawable.visibility_off),
                                        contentDescription = null,
                                        tint = if (passwordVisible) Color.White else Color.White.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Register Button
                        Button(
                            onClick = {
                                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                                    viewModel.registerWithEmail(name, email, phone, password)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .shadow(10.dp, RoundedCornerShape(28.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFFDC143C)
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            if (registerState is RegisterState.Loading) {
                                CircularProgressIndicator(color = Color(0xFFDC143C), modifier = Modifier.size(24.dp))
                            } else {
                                Text("Create Account", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Footer
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already a member?", color = Color.White.copy(alpha = 0.7f))
                    TextButton(onClick = onNavigateToLogin) {
                        Text("Sign In", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                if (registerState is RegisterState.Error) {
                    Text(
                        text = (registerState as RegisterState.Error).message,
                        color = Color(0xFFFFCDD2),
                        modifier = Modifier.padding(top = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: Int,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White.copy(alpha = 0.7f)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
            cursorColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White
            )
        }
    )
}
