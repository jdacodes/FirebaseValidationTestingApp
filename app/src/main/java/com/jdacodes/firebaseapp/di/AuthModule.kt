package com.jdacodes.firebaseapp.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jdacodes.firebaseapp.feature_auth.data.repository.AuthRepositoryImpl
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(
        auth = Firebase.auth
    )

    @Provides
    @Singleton
    fun provideSignUpUseCase(authRepository: AuthRepository): SignUpUseCase {
        return SignUpUseCase(authRepository)
    }
}