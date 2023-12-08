package com.jdacodes.firebaseapp.feature_auth.domain.util

interface EmailValidator {
    fun isValid(email: String): Boolean
}