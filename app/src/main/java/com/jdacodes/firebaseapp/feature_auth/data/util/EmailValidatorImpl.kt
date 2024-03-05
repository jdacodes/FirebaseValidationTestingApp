package com.jdacodes.firebaseapp.feature_auth.data.util

import androidx.core.util.PatternsCompat
import com.jdacodes.firebaseapp.feature_auth.domain.util.EmailValidator

class EmailValidatorImpl: EmailValidator {
    override fun isValid(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }
}