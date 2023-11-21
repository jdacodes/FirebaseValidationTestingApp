package com.jdacodes.firebaseapp.feature_auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
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
}