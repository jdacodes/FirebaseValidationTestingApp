package com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdacodes.firebaseapp.core.Constants
import com.jdacodes.firebaseapp.core.Constants.FORGOT_PASSWORD_FAILURE_MESSAGE
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import com.jdacodes.firebaseapp.feature_auth.domain.repository.SendPasswordResetEmailResponse
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.ValidateEmail
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.SignInFormState
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.SignInState
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.SignInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
//    private val repo: AuthRepository
    private val validateEmail: ValidateEmail,
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = _state.asStateFlow()

    var formState by mutableStateOf(ForgotPasswordFormState())

    var sendPasswordResetEmailResponse by mutableStateOf<SendPasswordResetEmailResponse>(
        Response.Success(
            false
        )
    )

    private val validationEventChannel = Channel<ForgotPasswordViewModel.ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onForgotPasswordResult(result: ForgotPasswordResult) {
        _state.update { it.copy(isLoading = true) }
        _state.update {
            it.copy(
                isForgotPasswordSuccessful = result.data == true,
                forgotPasswordError = result.errorMessage
            )
        }
        _state.update { it.copy(isLoading = false) }
    }

    fun resetState() {
        _state.update { ForgotPasswordState() }
        formState = formState.copy(
            email = Constants.EMPTY_STRING,
            emailError = null,
        )
    }

    fun onEvent(event: ForgotPasswordFormEvent) {
        when(event) {
            is ForgotPasswordFormEvent.EmailChanged -> {
                formState = formState.copy(email = event.email)
            }
            is ForgotPasswordFormEvent.Submit -> {
                viewModelScope.launch { submitData() }
            }

            else -> {}
        }
    }

    private suspend fun submitData() {
        _state.update { it.copy(isLoading = true) }
        val emailResult = validateEmail.execute(formState.email.trim())

        val hasError = emailResult.successful != true

        formState = formState.copy(
            emailError = emailResult.errorMessage,
        )

        if (hasError) {
            _state.update {
                it.copy(
                    isForgotPasswordSuccessful = false,
                    forgotPasswordError = FORGOT_PASSWORD_FAILURE_MESSAGE
                )
            }
            _state.update { it.copy(isLoading = false) }
            return
        }

        viewModelScope.launch {

            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    fun updateForgotPasswordState() {
        viewModelScope.launch {
            val result = validateEmail.firebaseSendPasswordResetEmail(
                formState.email.trim()
            )
            onForgotPasswordResult(result)
        }
    }

//    fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
//        sendPasswordResetEmailResponse = Response.Loading
//        sendPasswordResetEmailResponse = repo.sendPasswordResetEmail(email)
//    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

}