package com.jdacodes.firebaseapp.feature_auth.presentation

data class SignInFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
)
