package com.jdacodes.firebaseapp.core.util

sealed class Response<out T> {
    object Loading: Response<Nothing>()

    data class Success<out T>(
        val data: T
    ): Response<T>()

    data class Failure(
        val e: Exception
    ): Response<Nothing>()

    data class Error(
        val message: String
    ) : Response<Nothing>()
}
