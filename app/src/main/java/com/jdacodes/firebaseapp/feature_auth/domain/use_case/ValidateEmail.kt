package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import android.util.Patterns
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import com.jdacodes.firebaseapp.feature_auth.presentation.SignInResult


class ValidateEmail(private val repo: AuthRepository) {
    fun execute(email: String): ValidationSignInResult {
        if (email.isBlank()) {
            return ValidationSignInResult(
                successful = false,
                errorMessage = "The email can't be blank"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationSignInResult(
                successful = false,
                errorMessage = "That's not a valid email"
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