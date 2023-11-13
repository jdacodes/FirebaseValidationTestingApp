package com.jdacodes.firebaseapp.sign_in.domain.use_case

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
