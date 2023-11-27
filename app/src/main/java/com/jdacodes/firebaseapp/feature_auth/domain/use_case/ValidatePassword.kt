package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import com.jdacodes.firebaseapp.core.Constants.PASSWORD_LETTER_DIGIT_ERROR_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.PASSWORD_MINIMUM_CHARACTER_ERROR_MESSAGE


class ValidatePassword {
    fun execute(password: String): ValidationSignInResult {
        if(password.length < 8) {
            return ValidationSignInResult(
                successful = false,
                errorMessage = PASSWORD_MINIMUM_CHARACTER_ERROR_MESSAGE
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        if(!containsLettersAndDigits) {
            return ValidationSignInResult(
                successful = false,
                errorMessage = PASSWORD_LETTER_DIGIT_ERROR_MESSAGE
            )
        }
        return ValidationSignInResult(
            successful = true
        )
    }
}