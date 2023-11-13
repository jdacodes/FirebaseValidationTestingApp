package com.jdacodes.firebaseapp.sign_in.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdacodes.firebaseapp.core.domain.model.TextFieldState
import com.jdacodes.firebaseapp.sign_in.domain.use_case.ValidateEmail
import com.jdacodes.firebaseapp.sign_in.domain.use_case.ValidatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    var stateForm by mutableStateOf(SignInFormState())

    private val _usernameState = mutableStateOf(TextFieldState(text = "johndoe@example.com"))
    val usernameState: State<TextFieldState> = _usernameState

    fun setUsername(value: String) {
        _usernameState.value = usernameState.value.copy(text = value)
    }

    private val _passwordState = mutableStateOf(TextFieldState(text = "test123"))
    val passwordState: State<TextFieldState> = _passwordState

    fun setPassword(value: String) {
        _passwordState.value = _passwordState.value.copy(text = value)
    }

    private val _rememberMeState = mutableStateOf(false)
    val rememberMeState: State<Boolean> = _rememberMeState

    fun setRememberMe(value: Boolean) {
        _rememberMeState.value = value
    }

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(isLoading = true) }
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
        _state.update { it.copy(isLoading = false) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    fun onEvent(event: SignInFormEvent) {
        when (event) {
            is SignInFormEvent.EmailChanged -> {
                stateForm = stateForm.copy(email = event.email)
            }

            is SignInFormEvent.PasswordChanged -> {
                stateForm = stateForm.copy(password = event.password)
            }
            is SignInFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(stateForm.email)
        val passwordResult = validatePassword.execute(stateForm.password)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.successful }

        stateForm = stateForm.copy(
            emailError = emailResult.errorMessage,
            passwordError = passwordResult.errorMessage,
        )

        if(hasError) { return }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }

    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}