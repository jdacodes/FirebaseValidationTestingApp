package com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.googlefonts.isAvailableOnDevice
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jdacodes.firebaseapp.core.Constants
import com.jdacodes.firebaseapp.core.Constants.RESET_PASSWORD_MESSAGE
import com.jdacodes.firebaseapp.core.util.Utils.Companion.showMessage
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.components.ForgotPassword
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.components.ForgotPasswordContent
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.components.ForgotPasswordTopBar
import com.jdacodes.firebaseapp.ui.theme.provider
import kotlinx.coroutines.CoroutineExceptionHandler

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    state: ForgotPasswordState,
    formState: ForgotPasswordFormState

) {
    val TAG = "ForgotPasswordScreen"
    val context = LocalContext.current

    val usernameState = formState.email

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val handler = CoroutineExceptionHandler { _, throwable ->
        // process the Throwable
        Log.e(TAG, Constants.FONT_FAILURE_MESSAGE, throwable)
    }

    LaunchedEffect(key1 = state.forgotPasswordError) {
        state.forgotPasswordError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    CompositionLocalProvider(
        LocalFontFamilyResolver provides createFontFamilyResolver(LocalContext.current, handler)
    ) {

        LaunchedEffect(Unit) {
            if (provider.isAvailableOnDevice(context)) {
                Log.d(TAG, Constants.FONT_SUCCESSFUL_MESSAGE)
            }
        }
        Scaffold(
            topBar = {
                ForgotPasswordTopBar(
                    navigateBack = navigateBack
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            content = {

                ForgotPasswordContent(
                    usernameState = usernameState,
                    padding = PaddingValues(16.dp),
                    state = state,
                    formState = formState,
                    onUserNameTextChange = {
                        viewModel.onEvent(
                            ForgotPasswordFormEvent.EmailChanged(
                                it
                            )
                        )
                    },
                    onClickForgotPassword = { viewModel.onEvent(ForgotPasswordFormEvent.Submit) }
                )
            }
        )
    }
    ForgotPassword(
        navigateBack = navigateBack,
        showResetPasswordMessage = {
            showMessage(context, RESET_PASSWORD_MESSAGE)
        },
    )
}