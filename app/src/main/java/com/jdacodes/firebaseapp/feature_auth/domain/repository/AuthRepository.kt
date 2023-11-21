package com.jdacodes.firebaseapp.feature_auth.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.jdacodes.firebaseapp.core.util.Response

typealias SignUpResponse = Response<Boolean>
interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun firebaseSignUpWithEmailAndPassword(email: String, password: String): SignUpResponse

}