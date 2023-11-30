package com.jdacodes.firebaseapp.feature_auth.presentation.sign_up


data class SignUpResult(
    val data: UserSignUpData?,
    val errorMessage: String?,
)

data class UserSignUpData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)