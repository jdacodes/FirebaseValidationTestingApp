package com.jdacodes.firebaseapp.feature_auth.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.feature_auth.presentation.SignInResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

typealias SignUpResponse = Response<Boolean>
typealias SignInResponse = Response<Boolean>
typealias AuthStateResponse = StateFlow<Boolean>
typealias RevokeAccessResponse = Response<Boolean>
typealias ReloadUserResponse = Response<Boolean>
interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun firebaseSignUpWithEmailAndPassword(email: String, password: String): SignUpResponse
    suspend fun firebaseSignInWithEmailAndPassword(email: String, password: String): SignInResult

    suspend fun reloadFirebaseUser(): ReloadUserResponse
    fun signOut()
    suspend fun revokeAccess(): RevokeAccessResponse
    fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse
}