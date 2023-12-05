package com.jdacodes.firebaseapp.feature_auth.presentation.sign_up

data class SignUpState(
    val isSignUpSuccessful: Boolean = false,
    val  signUpError: String? = null,
    val isLoading: Boolean = false
)
