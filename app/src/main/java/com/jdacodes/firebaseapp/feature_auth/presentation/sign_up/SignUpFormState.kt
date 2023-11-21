package com.jdacodes.firebaseapp.feature_auth.presentation.sign_up

data class SignUpFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val acceptedTerms: Boolean = false,
    val termsError: String? = null,
    val isLoading: Boolean = false
)
