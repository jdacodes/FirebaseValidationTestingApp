package com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password

import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.SignInFormEvent

sealed class ForgotPasswordFormEvent {
    data class EmailChanged(val email: String) : ForgotPasswordFormEvent()
    object Submit: ForgotPasswordFormEvent()
}