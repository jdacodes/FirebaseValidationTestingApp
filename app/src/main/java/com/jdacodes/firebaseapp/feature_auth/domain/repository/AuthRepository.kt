package com.jdacodes.firebaseapp.feature_auth.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.ForgotPasswordResult
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.SignInResult
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_up.SignUpResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

typealias SignUpResponse = Response<Boolean>
typealias SignInResponse = Response<Boolean>
typealias AuthStateResponse = StateFlow<Boolean>
typealias RevokeAccessResponse = Response<Boolean>
typealias ReloadUserResponse = Response<Boolean>
typealias SendEmailVerificationResponse = Response<Boolean>
typealias SendPasswordResetEmailResponse = Response<Boolean>
interface AuthRepository {
    val currentUser: FirebaseUser?

//    suspend fun firebaseSignUpWithEmailAndPassword(email: String, password: String): SignUpResponse
    suspend fun firebaseSignUpWithEmailAndPassword(email: String, password: String): SignUpResult
    suspend fun firebaseSignInWithEmailAndPassword(email: String, password: String): SignInResult
    suspend fun reloadFirebaseUser(): ReloadUserResponse
    fun signOut()
    suspend fun revokeAccess(): RevokeAccessResponse
    fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse
    suspend fun sendEmailVerification(): SendEmailVerificationResponse

//    suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse
    suspend fun sendPasswordResetEmail(email: String): ForgotPasswordResult
}