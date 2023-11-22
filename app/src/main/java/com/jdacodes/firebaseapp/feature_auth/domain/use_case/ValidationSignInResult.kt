package com.jdacodes.firebaseapp.feature_auth.domain.use_case

data class ValidationSignInResult(
    val successful: Boolean? = false,
    val errorMessage: String? = null
)
