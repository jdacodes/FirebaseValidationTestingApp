package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository

class ValidateTerms() {
    fun execute(acceptedTerms: Boolean): ValidationSignInResult {
        if (!acceptedTerms) {
            return ValidationSignInResult(
                successful = false,
                errorMessage = "Please accept the terms"
            )
        }
        return ValidationSignInResult(
            successful = true
        )
    }
}