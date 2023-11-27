package com.jdacodes.firebaseapp.feature_auth.presentation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.googlefonts.isAvailableOnDevice
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdacodes.firebaseapp.R
import com.jdacodes.firebaseapp.core.Constants.FONT_FAILURE_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.FONT_SUCCESSFUL_MESSAGE
import com.jdacodes.firebaseapp.ui.theme.fontFamily
import com.jdacodes.firebaseapp.ui.theme.provider
import kotlinx.coroutines.CoroutineExceptionHandler

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    signInState: SignInState,
    signInFormState: SignInFormState,
    onClickSignInGoogle: () -> Unit,
    onClickSignInEmailAndPassword: () -> Unit,
    onClickDontHaveAccount: () -> Unit,
    onClickForgotPassword: () -> Unit,
    viewModel: SignInViewModel
) {
    val TAG = "SignInScreen"
    val context = LocalContext.current

    val usernameState = signInFormState.email
    val passwordState = signInFormState.password
    val rememberMeState = viewModel.rememberMeState.value

    val snackbarHostState = remember { SnackbarHostState() }

    val handler = CoroutineExceptionHandler { _, throwable ->
        // process the Throwable
        Log.e(TAG, FONT_FAILURE_MESSAGE, throwable)
    }
    CompositionLocalProvider(
        LocalFontFamilyResolver provides createFontFamilyResolver(LocalContext.current, handler)
    ) {

        LaunchedEffect(Unit) {
            if (provider.isAvailableOnDevice(context)) {
                Log.d(TAG, FONT_SUCCESSFUL_MESSAGE)
            }
        }

        LaunchedEffect(key1 = signInState.signInError) {
            signInState.signInError?.let { error ->
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Column(
                        Modifier
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "Sign In",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "Sign In to continue",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) {
            SignInScreenContent(
                usernameState = usernameState,
                passwordState = passwordState,
                rememberMeState = rememberMeState,
                signInState = signInState,
                signInFormState = signInFormState,
                onUserNameTextChange = {
//                viewModel.setUsername(it)
                    viewModel.onEvent(SignInFormEvent.EmailChanged(it))
                },
                onPasswordTextChange = {
//                viewModel.setPassword(it)
                    viewModel.onEvent(SignInFormEvent.PasswordChanged(it))
                },
                onRememberMeClicked = {
                    viewModel.setRememberMe(it)
                },
                onClickForgotPassword = {
                    onClickForgotPassword
                },
                onClickDontHaveAccount = onClickDontHaveAccount,
                onClickSignInGoogle = onClickSignInGoogle,
                onClickSignInEmailAndPassword = onClickSignInEmailAndPassword,
                onAuthComplete = { result ->
                    viewModel.onSignInResult(result = result)
//                    Toast.makeText(context, "It works!", Toast.LENGTH_SHORT).show()

                },
                onAuthError = { result ->
                    viewModel.onSignInResult(result = result)
                    Toast.makeText(context, "Try again later", Toast.LENGTH_SHORT)
                        .show()

                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInScreenContent(
    usernameState: String,
    passwordState: String,
    rememberMeState: Boolean,
    signInState: SignInState,
    signInFormState: SignInFormState,
    onUserNameTextChange: (String) -> Unit,
    onPasswordTextChange: (String) -> Unit,
    onRememberMeClicked: (Boolean) -> Unit,
    onClickForgotPassword: () -> Unit,
    onClickDontHaveAccount: () -> Unit,
    onClickSignInGoogle: () -> Unit,
    onClickSignInEmailAndPassword: () -> Unit,
    onAuthComplete: (SignInResult) -> Unit,
    onAuthError: (SignInResult) -> Unit,

    ) {
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
            errorBorderColor = MaterialTheme.colorScheme.error,
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
                        value = usernameState,
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
                        isError = signInFormState.emailError != null,
                        colors = outlineTextFieldColors
                    )
                    if (signInFormState.emailError != null) {
                        Text(
                            text = signInFormState.emailError ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
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
                        value = passwordState,
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
                        isError = signInFormState.passwordError != null,
                        colors = outlineTextFieldColors
                    )
                    if (signInFormState.passwordError != null) {
                        Text(
                            text = signInFormState.passwordError ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
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
                        Checkbox(checked = rememberMeState, onCheckedChange = {
                            onRememberMeClicked(it)
                        })
                        Text(
                            text = "Remember me",
                            fontSize = 12.sp,
                            fontFamily = fontFamily,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    TextButton(onClick = onClickForgotPassword) {
                        Text(
                            text = "Forgot password?",
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onClickSignInEmailAndPassword,
                    shape = CircleShape,
                    enabled = !signInState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp), text = "Sign In", textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = onClickDontHaveAccount,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Don't have an account?")
                            append(" ")
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                append("Sign Up")
                            }
                        },
                        fontFamily = fontFamily,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (signInState.isLoading) {
                        CircularProgressIndicator()
                    }
                }
            }

            item {
                //Google and Facebook Sign in IconButtons

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val containerModifier = Modifier.size(48.dp)
                        val iconModifier = Modifier.size(24.dp)
                        val backgroundColor = Color(0xFFB9B9B9).copy(alpha = 0.2f)
                        val contentColor = Color(0xFFB6B6B6)

                        OutlinedIconButton(
                            onClick = onClickSignInGoogle,
                            colors = IconButtonDefaults.outlinedIconButtonColors(
                                containerColor = backgroundColor,
                                contentColor = contentColor
                            ),
                            border = BorderStroke(width = 2.dp, color = contentColor),
                            modifier = containerModifier
                        ) {
                            Icon(
                                modifier = iconModifier,
                                painter = painterResource(id = R.drawable.ic_google_round),
                                tint = Color.Unspecified,
                                contentDescription = "Google"
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                                .padding(end = 16.dp)
                        )
                        FacebookButton(
                            onAuthComplete = onAuthComplete,
                            onAuthError = onAuthError,
                        )
                    }

                }
            }

        }


    }
}
