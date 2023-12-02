package com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password

data class ForgotPasswordFormState(
    val email: String = "",
    val emailError: String? = null,
)
