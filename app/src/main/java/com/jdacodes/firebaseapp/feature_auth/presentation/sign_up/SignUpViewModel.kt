package com.jdacodes.firebaseapp.feature_auth.presentation.sign_up

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdacodes.firebaseapp.core.Constants.SIGNUP_FAILURE_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.SIGNUP_SUCCESSFUL_MESSAGE
import com.jdacodes.firebaseapp.core.domain.model.CheckFieldState
import com.jdacodes.firebaseapp.core.domain.model.TextFieldState
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.core.util.UIEvents
import com.jdacodes.firebaseapp.feature_auth.domain.repository.SignUpResponse
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    var signUpResponse by mutableStateOf<SignUpResponse>(Response.Success(false))
        private set

    private val _usernameState = mutableStateOf(TextFieldState(text = ""))
    val usernameState: State<TextFieldState> = _usernameState
    fun setUsername(value: String) {
        _usernameState.value = usernameState.value.copy(text = value)
    }

    private val _passwordState = mutableStateOf(TextFieldState(text = ""))
    val passwordState: State<TextFieldState> = _passwordState
    fun setPassword(value: String) {
        _passwordState.value = _passwordState.value.copy(text = value)
    }

    private val _retypedPasswordState = mutableStateOf(TextFieldState(text = ""))
    val retypedPasswordState: State<TextFieldState> = _retypedPasswordState
    fun setRetypedPassword(value: String) {
        _retypedPasswordState.value = _retypedPasswordState.value.copy(text = value)
    }

    private val _acceptedTermsState = mutableStateOf(CheckFieldState(checked = false))
    val acceptedTermsState: State<CheckFieldState> = _acceptedTermsState
    fun setAcceptedTerms(value: Boolean) {
        _acceptedTermsState.value = _acceptedTermsState.value.copy(checked = value)
    }

    private val _eventFlow = MutableSharedFlow<UIEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun signUpWithEmailAndPassword() = viewModelScope.launch {
        signUpResponse = Response.Loading

        val signUpResult = signUpUseCase(
            username = usernameState.value.text,
            password = passwordState.value.text,
            retypedPassword = retypedPasswordState.value.text,
            acceptedTerms = acceptedTermsState.value.checked

        )

        signUpResponse = Response.notLoading

        if (signUpResult.usernameError != null) {
            _usernameState.value = usernameState.value.copy(error = signUpResult.usernameError)
        }

        if (signUpResult.passwordError != null) {
            _passwordState.value = passwordState.value.copy(error = signUpResult.passwordError)
        }

        if (signUpResult.retypedPasswordError != null) {
            _retypedPasswordState.value = retypedPasswordState.value.copy(error = signUpResult.retypedPasswordError)
        }

        if (signUpResult.retypedPasswordError != null) {
            _retypedPasswordState.value = retypedPasswordState.value.copy(error = signUpResult.retypedPasswordError)
        }

        if (signUpResult.acceptedTermsError != null) {
            _acceptedTermsState.value =  acceptedTermsState.value.copy(error = signUpResult.acceptedTermsError)
        }

        when(signUpResult.result){
            is Response.Success -> {
                resetState()
                _eventFlow.emit(
                    UIEvents.SnackBarEvent(
                        SIGNUP_SUCCESSFUL_MESSAGE
                    )
                )
            }
            is Response.Failure -> {
                UIEvents.SnackBarEvent(
                    SIGNUP_FAILURE_MESSAGE
                )
            }


            else -> {}
        }
    }

    fun resetState() {
        _usernameState.value = usernameState.value.copy(error = null)
        _passwordState.value = passwordState.value.copy(error = null)
        _retypedPasswordState.value = retypedPasswordState.value.copy(error = null)
        _acceptedTermsState.value = acceptedTermsState.value.copy(error = null)
    }

}