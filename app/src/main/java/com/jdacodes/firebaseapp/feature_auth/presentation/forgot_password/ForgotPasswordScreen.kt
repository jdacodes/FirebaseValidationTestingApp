package com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jdacodes.firebaseapp.core.Constants.RESET_PASSWORD_MESSAGE
import com.jdacodes.firebaseapp.core.util.Utils.Companion.showMessage
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.components.ForgotPassword
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.components.ForgotPasswordContent
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.components.ForgotPasswordTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val TAG = "ForgotPasswordScreen"
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ForgotPasswordTopBar(
                navigateBack = navigateBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
            ForgotPasswordContent(
                padding = padding,
                sendPasswordResetEmail = { email ->
                    viewModel.sendPasswordResetEmail(email)
                }
            )
        }
    )

    ForgotPassword(
        navigateBack = navigateBack,
        showResetPasswordMessage = {
            showMessage(context, RESET_PASSWORD_MESSAGE)
        },
        showErrorMessage = { errorMessage ->
            showMessage(context, errorMessage)
        }
    )
}