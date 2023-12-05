package com.jdacodes.firebaseapp.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jdacodes.firebaseapp.feature_auth.data.repository.AuthRepositoryImpl
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.SignUpUseCase
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.ValidateEmail
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.ValidatePassword
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.ValidateRepeatedPassword
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.ValidateTerms
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

//    @Provides
//    @Singleton
//    fun provideSignUpUseCase(authRepository: AuthRepository): SignUpUseCase {
//        return SignUpUseCase(authRepository)
//    }

    @Provides
    @Singleton
    fun provideValidateEmail(authRepository: AuthRepository): ValidateEmail {
        return ValidateEmail(authRepository)
    }

    @Provides
    @Singleton
    fun provideValidatePassword(): ValidatePassword {
        return ValidatePassword()
    }

    @Provides
    @Singleton
    fun provideValidateRepeatedPassword(): ValidateRepeatedPassword {
        return ValidateRepeatedPassword()
    }

    @Provides
    @Singleton
    fun provideValidateTerms(): ValidateTerms {
        return ValidateTerms()
    }
}