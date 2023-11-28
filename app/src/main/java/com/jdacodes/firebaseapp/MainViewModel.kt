package com.jdacodes.firebaseapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    init {
        getAuthState()
    }
    fun getAuthState() = repo.getAuthState(viewModelScope)

    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false
}