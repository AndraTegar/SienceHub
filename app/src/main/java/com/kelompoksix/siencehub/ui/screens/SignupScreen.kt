package com.kelompoksix.siencehub.ui.screens

package com.kelompoksix.siencehub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompoksix.siencehub.R

class WavyShapeSignup : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            lineTo(0f, size.height * 0.7f)
            cubicTo(
                size.width * 0.3f, size.height * 0.95f,
                size.width * 0.7f, size.height * 0.75f,
                size.width, size.height * 0.88f
            )
            lineTo(size.width, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    onCreateAccountSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val sageGreen = Color(0xFF7A8B76)
    val darkText = Color(0xFF333333)

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // Background melengkung dengan tekstur
        Image(
            painter = painterResource(id = R.drawable.texture_bg), // Menggunakan texture_bg
            contentDescription = "Background Wavy",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .clip(WavyShapeSignup()),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )

        Column(modifier = Modifier.fillMaxSize().padding(top = 180.dp)) {

            // Header Statis
            Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                Text("Sign up", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = darkText)
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.height(3.dp).width(60.dp).background(sageGreen))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form Input Scrollable
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 32.dp)
            ) {
                Text("Email", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = darkText)
                TextField(
                    value = email, onValueChange = { email = it },
                    placeholder = { Text("demo@email.com", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = sageGreen, unfocusedIndicatorColor = Color.LightGray),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("Phone no", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = darkText)
                TextField(
                    value = phone, onValueChange = { phone = it },
                    placeholder = { Text("+00 000-0000-000", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = sageGreen, unfocusedIndicatorColor = Color.LightGray),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("Password", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = darkText)
                TextField(
                    value = password, onValueChange = { password = it },
                    placeholder = { Text("enter your password", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(image, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = sageGreen, unfocusedIndicatorColor = Color.LightGray),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("Confirm Password", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = darkText)
                TextField(
                    value = confirmPassword, onValueChange = { confirmPassword = it },
                    placeholder = { Text("Confirm your password", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) { Icon(image, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = sageGreen, unfocusedIndicatorColor = Color.LightGray),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onCreateAccountSuccess,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = sageGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Create Account", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Gray)) { append("Already have an Account! ") }
                        withStyle(style = SpanStyle(color = sageGreen, fontWeight = FontWeight.Bold)) { append("Login") }
                    },
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 32.dp).clickable { onNavigateToLogin() }
                )
            }
        }
    }
}