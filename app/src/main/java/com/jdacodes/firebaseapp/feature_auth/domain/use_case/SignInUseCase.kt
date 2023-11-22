package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import android.util.Patterns
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import com.jdacodes.firebaseapp.feature_auth.presentation.SignInResult

class SignInUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String,
    ): SignInResult {

        val usernameError = if (username.isBlank()) {
            "The email can't be blank"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            "That's not a valid email"
        } else null

        if (usernameError != null) {
//            return ValidationResult(
//                usernameError = usernameError
//            )
            return SignInResult(
                data = null,
                errorMessage = usernameError
            )
        }

        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        val passwordError = if (password.length < 7) {
            "The password needs to consist of at least 7 characters"
        } else if (!containsLettersAndDigits) {
            "The password needs to contain at least one letter and digit"
        } else null

        if (passwordError != null) {
//            return ValidationResult(
//                passwordError = passwordError
//            )

            return SignInResult(
                data = null,
                errorMessage = passwordError
            )
        }

//        return ValidationResult(
//            result = repo.firebaseSignInWithEmailAndPassword(username, password)
//        )
        return repo.firebaseSignInWithEmailAndPassword(email = username, password = password)
    }
}