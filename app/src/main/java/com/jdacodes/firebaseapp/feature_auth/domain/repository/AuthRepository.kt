package com.jdacodes.firebaseapp.feature_auth.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.feature_auth.presentation.SignInResult

typealias SignUpResponse = Response<Boolean>
typealias SignInResponse = Response<Boolean>
interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun firebaseSignUpWithEmailAndPassword(email: String, password: String): SignUpResponse
    suspend fun firebaseSignInWithEmailAndPassword(email: String, password: String): SignInResult

}