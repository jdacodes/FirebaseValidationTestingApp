package com.jdacodes.firebaseapp.feature_auth.presentation.sign_up.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jdacodes.firebaseapp.core.components.ProgressBar
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.ForgotPasswordViewModel
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_up.SignUpViewModel

@Composable
fun SignUp(
    viewModel: SignUpViewModel = hiltViewModel(),
    sendEmailVerification: () -> Unit,
    showVerifyEmailMessage: () -> Unit
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = state.isSignUpSuccessful) {
        if (state.isSignUpSuccessful) {
            sendEmailVerification()
            showVerifyEmailMessage()
            viewModel.resetState()

        }
    }

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is SignUpViewModel.ValidationEvent.Success -> {
                    viewModel.updateSignUpState()

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


//    when(val signUpResponse = viewModel.signUpResponse) {
//        is Response.Loading -> ProgressBar()
//        is Response.Success -> {
//            val isUserSignedUp = signUpResponse.data
//            LaunchedEffect(isUserSignedUp) {
//                if (isUserSignedUp) {
//                    sendEmailVerification()
//                    showVerifyEmailMessage()
//                }
//            }
//        }
//        is Response.Failure -> signUpResponse.apply {
//            LaunchedEffect(e) {
//                print(e)
//            }
//        }
//
//        else -> {}
//    }
}