package com.jdacodes.firebaseapp.feature_auth.presentation.sign_up

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jdacodes.firebaseapp.core.Constants.EMPTY_STRING
import com.jdacodes.firebaseapp.core.Constants.SIGNUP_SUCCESSFUL_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.VERIFY_EMAIL_MESSAGE
import com.jdacodes.firebaseapp.core.domain.model.CheckFieldState
import com.jdacodes.firebaseapp.core.domain.model.TextFieldState
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.core.util.UIEvents
import com.jdacodes.firebaseapp.core.util.Utils.Companion.showMessage
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_up.components.SignUp
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_up.components.SignUpTopBar
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navigateBack: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val usernameState = viewModel.usernameState.value
    val passwordState = viewModel.passwordState.value
    val repeatedPasswordState = viewModel.retypedPasswordState.value
    val acceptedTermsState = viewModel.acceptedTermsState.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvents.SnackBarEvent -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            SignUpTopBar(navigateBack = navigateBack)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
            SignUpScreenContent(
                usernameState = usernameState,
                passwordState = passwordState,
                repeatedPasswordState = repeatedPasswordState,
                acceptedTermsState = acceptedTermsState,
                onUserNameTextChange = { viewModel.setUsername(it) },
                onPasswordTextChange = { viewModel.setPassword(it) },
                onRepeatPasswordTextChange = { viewModel.setRetypedPassword(it) },
                onAcceptedTermsClicked = { viewModel.setAcceptedTerms(it) },
                padding = padding,
                onClickSignUp = {
                    viewModel.signUpWithEmailAndPassword()
                },
                navigateBack = navigateBack,
                viewModel = viewModel

            )
        }
    )

//    SendEmailVerification()

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreenContent(
    usernameState: TextFieldState,
    passwordState: TextFieldState,
    repeatedPasswordState: TextFieldState,
    acceptedTermsState: CheckFieldState,
    onUserNameTextChange: (String) -> Unit,
    onPasswordTextChange: (String) -> Unit,
    onRepeatPasswordTextChange: (String) -> Unit,
    onAcceptedTermsClicked: (Boolean) -> Unit,
    onClickSignUp: () -> Unit,
    padding: PaddingValues,
    navigateBack: () -> Unit,
    viewModel: SignUpViewModel

) {

    val context = LocalContext.current

    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(
                value = TextFieldValue(
                    text = EMPTY_STRING
                )
            )
        }
    )
    var password by rememberSaveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(
                value = TextFieldValue(
                    text = EMPTY_STRING
                )
            )
        }
    )

    var retypedPassword by rememberSaveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(
                value = TextFieldValue(
                    text = EMPTY_STRING
                )
            )
        }
    )

    var acceptedTerms by rememberSaveable {
        mutableStateOf(false)
    }
    val keyboard = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .padding(16.dp),
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        val outlineTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            selectionColors = TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.primary
            )
        )

        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            item {
                Spacer(modifier = Modifier.height(64.dp))
                Column {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = usernameState.text,
                        onValueChange = {
                            onUserNameTextChange(it)
                        },
                        label = {
                            Text(
                                text = "Username",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        maxLines = 1,
                        singleLine = true,
                        isError = usernameState.error != null,
                        colors = outlineTextFieldColors
                    )
                    if (usernameState.error != null) {
                        Text(
                            text = usernameState.error ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onError,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                var passwordVisible by rememberSaveable { mutableStateOf(false) }
                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = passwordState.text,
                        onValueChange = {
                            onPasswordTextChange(it)
                        },
                        label = {
                            Text(
                                text = "Password",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                        ),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            // Please provide localized description for accessibility services
                            val description =
                                if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, description)
                            }
                        },
                        maxLines = 1,
                        singleLine = true,
                        isError = passwordState.error != null,
                        colors = outlineTextFieldColors
                    )
                    if (passwordState.error != null) {
                        Text(
                            text = passwordState.error ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onError,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                var passwordVisible by rememberSaveable { mutableStateOf(false) }
                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = repeatedPasswordState.text,
                        onValueChange = {
                            onRepeatPasswordTextChange(it)
                        },
                        label = {
                            Text(
                                text = "Repeat Password",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                        ),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            // Please provide localized description for accessibility services
                            val description =
                                if (passwordVisible) "Hide repeated password" else "Show repeated password"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, description)
                            }
                        },
                        maxLines = 1,
                        singleLine = true,
                        isError = passwordState.error != null,
                        colors = outlineTextFieldColors
                    )
                    if (repeatedPasswordState.error != null) {
                        Text(
                            text = repeatedPasswordState.error ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onError,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = acceptedTermsState.checked, onCheckedChange = {
                            onAcceptedTermsClicked(it)
                        })
                        Text(
                            text = "Accepted Terms of Service",
                            fontSize = 12.sp,
//                           fontFamily = workSans,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
                if (acceptedTermsState.error != null) {
                    Text(
                        text = acceptedTermsState.error ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onError,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onClickSignUp,
                    shape = CircleShape,
                    enabled = when (viewModel.signUpResponse) {
                        is Response.Loading -> false
                        else -> {
                            true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp), text = "Sign Up", textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SignUp(
                        sendEmailVerification = {
//            viewModel.sendEmailVerification()
                        },
                        showVerifyEmailMessage = {
                            showMessage(context, SIGNUP_SUCCESSFUL_MESSAGE)
                        }
                    )
                }
            }
        }
    }
}
