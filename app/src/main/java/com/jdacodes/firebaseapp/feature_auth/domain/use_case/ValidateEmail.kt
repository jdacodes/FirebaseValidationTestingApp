package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import android.util.Patterns
import com.jdacodes.firebaseapp.core.Constants.EMAIL_BLANK_ERROR_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.EMAIL_INVALID_ERROR_MESSAGE
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.SignInResult


class ValidateEmail(private val repo: AuthRepository) {
    fun execute(email: String): ValidationSignInResult {
        if (email.isBlank()) {
            return ValidationSignInResult(
                successful = false,
                errorMessage = EMAIL_BLANK_ERROR_MESSAGE
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationSignInResult(
                successful = false,
                errorMessage = EMAIL_INVALID_ERROR_MESSAGE
            )
        }
        return ValidationSignInResult(
            successful = true
        )

    }

    suspend fun firebaseSignInWithEmailAndPassword(email: String, password: String): SignInResult {
        return repo.firebaseSignInWithEmailAndPassword(email, password)
    }
}