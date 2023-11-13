package com.jdacodes.firebaseapp.sign_in.presentation

sealed class SignInFormEvent {
    data class EmailChanged(val email: String) : SignInFormEvent()
    data class PasswordChanged(val password: String) : SignInFormEvent()
    object Submit: SignInFormEvent()
}
