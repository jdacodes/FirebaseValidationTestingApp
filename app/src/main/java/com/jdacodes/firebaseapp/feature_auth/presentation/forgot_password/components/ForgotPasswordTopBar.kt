package com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jdacodes.firebaseapp.core.Constants.FORGOT_PASSWORD_SCREEN
import com.jdacodes.firebaseapp.core.components.BackIcon
import com.jdacodes.firebaseapp.ui.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = FORGOT_PASSWORD_SCREEN,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
                color = MaterialTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            BackIcon (
                navigateBack = navigateBack
            )
        }
    )
}