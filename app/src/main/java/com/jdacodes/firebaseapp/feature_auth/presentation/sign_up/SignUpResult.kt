package com.jdacodes.firebaseapp.feature_auth.presentation.sign_up


data class SignUpResult(
    val data: Boolean? = false,
    val errorMessage: String?,
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)