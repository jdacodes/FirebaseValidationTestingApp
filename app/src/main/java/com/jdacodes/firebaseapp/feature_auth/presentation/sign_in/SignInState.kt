package com.jdacodes.firebaseapp.feature_auth.presentation.sign_in

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val  signInError: String? = null,
    val isLoading: Boolean = false
)
