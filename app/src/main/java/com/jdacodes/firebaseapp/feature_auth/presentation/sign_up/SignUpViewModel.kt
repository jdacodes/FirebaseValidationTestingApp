package com.jdacodes.firebaseapp.feature_auth.presentation.sign_up

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdacodes.firebaseapp.core.Constants
import com.jdacodes.firebaseapp.core.Constants.EMPTY_STRING
import com.jdacodes.firebaseapp.core.Constants.SIGNUP_FAILURE_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.SIGNUP_SUCCESSFUL_MESSAGE
import com.jdacodes.firebaseapp.core.domain.model.CheckFieldState
import com.jdacodes.firebaseapp.core.domain.model.TextFieldState
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.core.util.UIEvents
import com.jdacodes.firebaseapp.feature_auth.domain.repository.SendEmailVerificationResponse
import com.jdacodes.firebaseapp.feature_auth.domain.repository.SignUpResponse
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.SignUpUseCase
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.ValidateEmail
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.ValidatePassword
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.ValidateRepeatedPassword
import com.jdacodes.firebaseapp.feature_auth.domain.use_case.ValidateTerms
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.ForgotPasswordFormState
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.ForgotPasswordState
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.ForgotPasswordViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
//    private val signUpUseCase: SignUpUseCase
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword,
    private val validateTerms: ValidateTerms
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    var formState by mutableStateOf(SignUpFormState())
    var signUpResponse by mutableStateOf<SignUpResponse>(Response.Success(false))
        private set

    var sendEmailVerificationResponse by mutableStateOf<SendEmailVerificationResponse>(
        Response.Success(
            false
        )
    )
        private set

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

//    private val _usernameState = mutableStateOf(TextFieldState(text = ""))
//    val usernameState: State<TextFieldState> = _usernameState
//    fun setUsername(value: String) {
//        _usernameState.value = usernameState.value.copy(text = value)
//    }

//    private val _passwordState = mutableStateOf(TextFieldState(text = ""))
//    val passwordState: State<TextFieldState> = _passwordState
//    fun setPassword(value: String) {
//        _passwordState.value = _passwordState.value.copy(text = value)
//    }

//    private val _retypedPasswordState = mutableStateOf(TextFieldState(text = ""))
//    val retypedPasswordState: State<TextFieldState> = _retypedPasswordState
//    fun setRetypedPassword(value: String) {
//        _retypedPasswordState.value = _retypedPasswordState.value.copy(text = value)
//    }

//    private val _acceptedTermsState = mutableStateOf(CheckFieldState(checked = false))
//    val acceptedTermsState: State<CheckFieldState> = _acceptedTermsState
//    fun setAcceptedTerms(value: Boolean) {
//        _acceptedTermsState.value = _acceptedTermsState.value.copy(checked = value)
//    }

//    private val _eventFlow = MutableSharedFlow<UIEvents>()
//    val eventFlow = _eventFlow.asSharedFlow()

    fun onSignUpResult(result: SignUpResult) {
        _state.update { it.copy(isLoading = true) }
        _state.update {
            it.copy(
                isSignUpSuccessful = result.data == true,
                signUpError = result.errorMessage
            )
        }
        _state.update { it.copy(isLoading = false) }
    }

    fun onEvent(event: SignUpFormEvent) {
        when (event) {
            is SignUpFormEvent.EmailChanged -> {
                formState = formState.copy(email = event.email)
            }

            is SignUpFormEvent.PasswordChanged -> {
                formState = formState.copy(password = event.password)
            }

            is SignUpFormEvent.RepeatedPasswordChanged -> {
                formState = formState.copy(repeatedPassword = event.repeatedPassword)
            }

            is SignUpFormEvent.AcceptTerms -> {
                formState = formState.copy(acceptedTerms = event.isAccepted)
            }

            is SignUpFormEvent.Submit -> {
                viewModelScope.launch { submitData() }
            }

            else -> {}
        }
    }

    private suspend fun submitData() {
        _state.update { it.copy(isLoading = true) }
        val emailResult = validateEmail.execute(formState.email.trim())
        val passwordResult = validatePassword.execute(formState.password.trim())
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            repeatedPassword = formState.repeatedPassword.trim(),
            password = formState.password.trim()
        )
        val termsResult = validateTerms.execute(formState.acceptedTerms)

        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            termsResult
        ).any { !it.successful!! }

        formState = formState.copy(
            emailError = emailResult.errorMessage,
            passwordError = passwordResult.errorMessage,
            repeatedPasswordError = repeatedPasswordResult.errorMessage,
            termsError = termsResult.errorMessage
        )

        if (hasError) {
            _state.update {
                it.copy(
                    isSignUpSuccessful = false,
                    signUpError = Constants.SIGNUP_FAILURE_MESSAGE
                )
            }
            _state.update { it.copy(isLoading = false) }
            return
        }

        viewModelScope.launch {

            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    fun updateSignUpState() {
        viewModelScope.launch {
            val result = validateEmail.firebaseSignUpEmailAndPassword(
                formState.email.trim(),
                formState.password.trim(),
            )
            onSignUpResult(result)
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }



//    fun signUpWithEmailAndPassword() = viewModelScope.launch {
//        signUpResponse = Response.Loading
//
//        val signUpResult = signUpUseCase(
//            username = usernameState.value.text.trim(),
//            password = passwordState.value.text.trim(),
//            retypedPassword = retypedPasswordState.value.text.trim(),
//            acceptedTerms = acceptedTermsState.value.checked
//
//        )

//        signUpResponse = Response.notLoading

//        if (signUpResult.usernameError != null) {
//            _usernameState.value = usernameState.value.copy(error = signUpResult.usernameError)
//        }
//
//        if (signUpResult.passwordError != null) {
//            _passwordState.value = passwordState.value.copy(error = signUpResult.passwordError)
//        }
//
//        if (signUpResult.retypedPasswordError != null) {
//            _retypedPasswordState.value =
//                retypedPasswordState.value.copy(error = signUpResult.retypedPasswordError)
//        }
//
//        if (signUpResult.retypedPasswordError != null) {
//            _retypedPasswordState.value =
//                retypedPasswordState.value.copy(error = signUpResult.retypedPasswordError)
//        }
//
//        if (signUpResult.acceptedTermsError != null) {
//            _acceptedTermsState.value =
//                acceptedTermsState.value.copy(error = signUpResult.acceptedTermsError)
//        }
//
//        when (signUpResult.result) {
//            is Response.Success -> {
//                resetState()
//                _eventFlow.emit(
//                    UIEvents.SnackBarEvent(
//                        SIGNUP_SUCCESSFUL_MESSAGE
//                    )
//                )
//            }
//
//            is Response.Failure -> {
//                UIEvents.SnackBarEvent(
//                    SIGNUP_FAILURE_MESSAGE
//                )
//            }
//
//            else -> {}
//        }
//    }

    fun sendEmailVerification() = viewModelScope.launch {
        sendEmailVerificationResponse = Response.Loading
//        _state.update { it.copy(isLoading = true) }
//        sendEmailVerificationResponse = signUpUseCase.sendEmailVerification()
        sendEmailVerificationResponse = validateEmail.firebaseSendEmailVerification()
        Log.d("sendEmailVerification", "Email sent.")

    }

    fun resetState() {
        _state.update { SignUpState() }
        formState = formState.copy(
            email = EMPTY_STRING,
            emailError = null,
            password = EMPTY_STRING,
            passwordError = null,
            repeatedPassword = EMPTY_STRING,
            repeatedPasswordError = null,
            acceptedTerms = false,
            termsError = null,
        )
//        _usernameState.value = usernameState.value.copy(error = null, text = EMPTY_STRING)
//        _passwordState.value = passwordState.value.copy(error = null, text = EMPTY_STRING)
//        _retypedPasswordState.value =
//            retypedPasswordState.value.copy(error = null, text = EMPTY_STRING)
//        _acceptedTermsState.value = acceptedTermsState.value.copy(error = null, checked = false)
    }

}