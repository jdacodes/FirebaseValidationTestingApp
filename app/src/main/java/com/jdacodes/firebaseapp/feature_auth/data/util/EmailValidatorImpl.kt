package com.jdacodes.firebaseapp.feature_auth.data.util

import android.util.Patterns
import com.jdacodes.firebaseapp.feature_auth.domain.util.EmailValidator

class EmailValidatorImpl: EmailValidator {
    override fun isValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}