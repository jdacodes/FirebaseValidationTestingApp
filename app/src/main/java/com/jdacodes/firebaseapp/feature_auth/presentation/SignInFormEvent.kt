package com.jdacodes.firebaseapp.feature_auth.presentation

sealed class SignInFormEvent {
    data class EmailChanged(val email: String) : SignInFormEvent()
    data class PasswordChanged(val password: String) : SignInFormEvent()
    object Submit: SignInFormEvent()
}
