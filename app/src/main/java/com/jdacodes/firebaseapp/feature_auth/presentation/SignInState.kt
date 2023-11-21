package com.jdacodes.firebaseapp.feature_auth.presentation

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val  signInError: String? = null,
    val isLoading: Boolean = false
)
