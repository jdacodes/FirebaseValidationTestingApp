package com.jdacodes.firebaseapp.feature_auth.presentation.sign_up

sealed class SignUpFormEvent {
    data class EmailChanged(val email: String) : SignUpFormEvent()
    data class PasswordChanged(val password: String) : SignUpFormEvent()
    data class RepeatedPasswordChanged(
        val repeatedPassword: String
    ) : SignUpFormEvent()

    data class AcceptTerms(val isAccepted: Boolean) : SignUpFormEvent()

    object Submit: SignUpFormEvent()
}
