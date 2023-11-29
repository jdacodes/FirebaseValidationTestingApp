package com.jdacodes.firebaseapp.feature_auth.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthStateResponse
import com.jdacodes.firebaseapp.feature_auth.domain.repository.ReloadUserResponse
import com.jdacodes.firebaseapp.feature_auth.domain.repository.RevokeAccessResponse
import com.jdacodes.firebaseapp.feature_auth.domain.repository.SendEmailVerificationResponse
import com.jdacodes.firebaseapp.feature_auth.domain.repository.SignInResponse
import com.jdacodes.firebaseapp.feature_auth.presentation.SignInResult
import com.jdacodes.firebaseapp.feature_auth.presentation.UserData
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
):AuthRepository {
    override val currentUser get() = auth.currentUser
    override suspend fun firebaseSignUpWithEmailAndPassword(
        email: String, password: String) = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
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
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
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
}
