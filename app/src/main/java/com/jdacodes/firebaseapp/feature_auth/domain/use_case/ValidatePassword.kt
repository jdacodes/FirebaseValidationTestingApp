package com.jdacodes.firebaseapp.feature_auth.domain.use_case


class ValidatePassword {
    fun execute(password: String): ValidationSignInResult {
        if(password.length < 7) {
            return ValidationSignInResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 7 characters"
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        if(!containsLettersAndDigits) {
            return ValidationSignInResult(
                successful = false,
                errorMessage = "The password needs to contain at least one letter and digit"
            )
        }
        return ValidationSignInResult(
            successful = true
        )
    }
}