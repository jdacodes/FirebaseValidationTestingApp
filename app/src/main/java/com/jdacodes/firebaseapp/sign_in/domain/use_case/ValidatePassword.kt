package com.jdacodes.firebaseapp.sign_in.domain.use_case

import android.util.Patterns


class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if(password.length < 7) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 7 characters"
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        if(!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to contain at least one letter and digit"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}