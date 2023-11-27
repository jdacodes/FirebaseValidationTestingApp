package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import android.util.Patterns
import com.jdacodes.firebaseapp.core.Constants.EMAIL_BLANK_ERROR_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.EMAIL_INVALID_ERROR_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.PASSWORD_LETTER_DIGIT_ERROR_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.PASSWORD_MINIMUM_CHARACTER_ERROR_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.PASSWORD_NOT_MATCH_ERROR_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.TERMS_NOT_ACCEPTED_ERROR_MESSAGE
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
class SignUpUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        retypedPassword: String,
        acceptedTerms: Boolean
    ): ValidationSignUpResult {

        val usernameError = if (username.isBlank()) {
            EMAIL_BLANK_ERROR_MESSAGE
        } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            EMAIL_INVALID_ERROR_MESSAGE
        } else null

        if (usernameError != null) {
            return ValidationSignUpResult(
                usernameError = usernameError
            )
        }

        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        val passwordError =   if(password.length < 8) {
            PASSWORD_MINIMUM_CHARACTER_ERROR_MESSAGE
        }else if(!containsLettersAndDigits){
            PASSWORD_LETTER_DIGIT_ERROR_MESSAGE
        } else null

        if (passwordError != null) {
            return ValidationSignUpResult(
                passwordError = passwordError
            )
        }

        val retypedPasswordError = if(password != retypedPassword) PASSWORD_NOT_MATCH_ERROR_MESSAGE else null
        if (retypedPasswordError != null) {
            return ValidationSignUpResult(
                retypedPasswordError = retypedPasswordError
            )
        }

         val acceptedTermsError = if(!acceptedTerms) TERMS_NOT_ACCEPTED_ERROR_MESSAGE else null
        if (acceptedTermsError != null) {
            return ValidationSignUpResult(
                acceptedTermsError = acceptedTermsError
            )
        }


        return ValidationSignUpResult(
            result = repo.firebaseSignUpWithEmailAndPassword(username, password)
        )
    }
}