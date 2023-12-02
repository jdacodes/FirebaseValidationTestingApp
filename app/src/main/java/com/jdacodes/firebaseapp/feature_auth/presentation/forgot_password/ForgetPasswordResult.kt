package com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password

data class ForgotPasswordResult(
    val data: Boolean? = false,
    val errorMessage: String?,
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)
