package com.jdacodes.firebaseapp.sign_in.presentation

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val  signInError: String? = null,
    val isLoading: Boolean = false
)
