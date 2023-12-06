package com.jdacodes.firebaseapp.feature_auth.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.ForgotPasswordResult
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.SignInResult
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.UserData
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_up.SignUpResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override val currentUser get() = auth.currentUser
    override suspend fun firebaseSignUpWithEmailAndPassword(
        email: String, password: String
    ) = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        SignUpResult(
            data = true,
            errorMessage = null
        )
    } catch (e: Exception) {
        e.printStackTrace()
        if (e is CancellationException) throw e
        SignUpResult(
            data = false,
            errorMessage = e.message
        )
    }

    override suspend fun firebaseSignInWithEmailAndPassword(
        email: String,
        password: String
    ) = try {
        val user = auth.signInWithEmailAndPassword(email, password).await().user
        SignInResult(
            data = user?.run {
                UserData(
                    userId = uid,
                    username = displayName,
                    profilePictureUrl = photoUrl?.toString()
                )
            },
            errorMessage = null
        )
    } catch (e: Exception) {
        e.printStackTrace()
        if (e is CancellationException) throw e
        SignInResult(
            data = null,
            errorMessage = e.message
        )
    }

    override suspend fun reloadFirebaseUser() = try {
        auth.currentUser?.reload()?.await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override fun signOut() = auth.signOut()
    override suspend fun revokeAccess() = try {
        auth.currentUser?.delete()?.await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override fun getAuthState(viewModelScope: CoroutineScope) = callbackFlow {
        val authStateListener = AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser == null)

    override suspend fun sendEmailVerification() = try {
        auth.currentUser?.sendEmailVerification()?.await()
        Log.d("sendEmailVerification", "Email sent.")
        Response.Success(true)

    } catch (e: Exception) {
        Log.d("sendEmailVerification", "Email not sent.")
        Response.Failure(e)
    }

    override suspend fun sendPasswordResetEmail(email: String) = try {
        val user = auth.sendPasswordResetEmail(email).await()
        ForgotPasswordResult(
            data = true,
            errorMessage = null
        )
    } catch (e: Exception) {
        e.printStackTrace()
        if (e is CancellationException) throw e
        ForgotPasswordResult(
            data = false,
            errorMessage = e.message
        )
    }
}
