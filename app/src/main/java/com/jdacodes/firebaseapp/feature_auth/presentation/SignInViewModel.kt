package com.jdacodes.firebaseapp.feature_auth.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdacodes.firebaseapp.core.domain.model.TextFieldState
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.SignInUseCase
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.SignUpUseCase
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.ValidateEmail
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.ValidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
//    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    var formState by mutableStateOf(SignInFormState())

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
                formState = formState.copy(email = event.email)
            }

            is SignInFormEvent.PasswordChanged -> {
                formState = formState.copy(password = event.password)
            }

            is SignInFormEvent.Submit -> {
                viewModelScope.launch { submitData() }

            }
        }
    }

    private suspend fun submitData() {
        _state.update { it.copy(isLoading = true) }
        val emailResult = validateEmail.execute(formState.email)
        val passwordResult = validatePassword.execute(formState.password)

//        val signInResult = signInUseCase(
//            username = formState.email.trim(),
//            password = formState.password.trim()
//        )

//        val hasError = listOf(
//            emailResult,
//            passwordResult
//        ).any { !it.successful }

        val hasError = !(emailResult.successful == true && passwordResult.successful == true)


        formState = formState.copy(
            emailError = emailResult.errorMessage,
            passwordError = passwordResult.errorMessage,
        )



        if (hasError) {
            _state.update {
                it.copy(
                    isSignInSuccessful = false,
                    signInError = formState.emailError.toString()
                )
            }
            _state.update { it.copy(isLoading = false) }
            return
        }

        viewModelScope.launch {

            validationEventChannel.send(ValidationEvent.Success)
        }

    }

    fun updateSignInState() {

        viewModelScope.launch {
            val result = validateEmail.firebaseSignInWithEmailAndPassword(
                formState.email,
                formState.password
            )
            onSignInResult(result)
        }

    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}