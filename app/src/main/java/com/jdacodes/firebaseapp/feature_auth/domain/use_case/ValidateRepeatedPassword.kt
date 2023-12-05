package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository

class ValidateRepeatedPassword() {
    fun execute(password: String, repeatedPassword: String): ValidationSignInResult {
        if (password != repeatedPassword) {
            return ValidationSignInResult(
                successful = false,
                errorMessage = "The passwords don't match"
            )
        }
        return ValidationSignInResult(
            successful = true
        )
    }
}