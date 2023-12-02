package com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jdacodes.firebaseapp.core.components.ProgressBar
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.ForgotPasswordViewModel
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.SignInViewModel

@Composable
fun ForgotPassword(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    showResetPasswordMessage: () -> Unit,

) {

    val forgotPasswordState by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = forgotPasswordState.isForgotPasswordSuccessful) {
        if (forgotPasswordState.isForgotPasswordSuccessful) {
            showResetPasswordMessage()
            viewModel.resetState()
            navigateBack()

        }

    }
    //Check for Sign in Validation event is successful
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ForgotPasswordViewModel.ValidationEvent.Success -> {
                    viewModel.updateForgotPasswordState()

//                                            Toast.makeText(
//                                                context,
//                                                "Registration successful",
//                                                Toast.LENGTH_LONG
//                                            ).show()

                }

                else -> {
                }
            }
        }
    }

}