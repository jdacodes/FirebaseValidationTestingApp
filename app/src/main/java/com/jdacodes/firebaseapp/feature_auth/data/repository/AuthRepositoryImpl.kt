package com.jdacodes.firebaseapp.feature_auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import com.jdacodes.firebaseapp.feature_auth.domain.repository.SignInResponse
import com.jdacodes.firebaseapp.feature_auth.presentation.SignInResult
import com.jdacodes.firebaseapp.feature_auth.presentation.UserData
import kotlinx.coroutines.CancellationException
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
}
