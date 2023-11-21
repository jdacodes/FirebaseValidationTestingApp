package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.feature_auth.domain.repository.SignUpResponse

data class ValidationResult(
    val passwordError: String? = null,
    val usernameError: String? = null,
    val retypedPasswordError: String? = null,
    val acceptedTermsError: String? = null,
    val result: SignUpResponse? = null
)