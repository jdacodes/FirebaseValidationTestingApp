package com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password

sealed class ForgotPasswordFormEvent {
    data class EmailChanged(val email: String) : ForgotPasswordFormEvent()
    object Submit: ForgotPasswordFormEvent()
}