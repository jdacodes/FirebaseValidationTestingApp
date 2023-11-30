package com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.jdacodes.firebaseapp.core.Constants.FORGOT_PASSWORD_SCREEN
import com.jdacodes.firebaseapp.core.components.BackIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = FORGOT_PASSWORD_SCREEN
            )
        },
        navigationIcon = {
            BackIcon (
                navigateBack = navigateBack
            )
        }
    )
}