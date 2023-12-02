package com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password

data class ForgotPasswordState(
    val isForgotPasswordSuccessful: Boolean = false,
    val forgotPasswordError: String? = null,
    val isLoading: Boolean = false
)
