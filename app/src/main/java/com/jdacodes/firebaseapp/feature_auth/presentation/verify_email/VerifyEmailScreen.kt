package com.jdacodes.firebaseapp.feature_auth.presentation.verify_email

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jdacodes.firebaseapp.core.Constants.EMAIL_NOT_VERIFIED_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.VERIFY_EMAIL_SCREEN
import com.jdacodes.firebaseapp.core.components.TopBar
import com.jdacodes.firebaseapp.core.util.Utils.Companion.showMessage
import com.jdacodes.firebaseapp.feature_auth.presentation.verify_email.component.ReloadUser
import com.jdacodes.firebaseapp.feature_auth.presentation.verify_email.component.VerifyEmailScreenContent
import com.jdacodes.firebaseapp.profile.ProfileViewModel
import com.jdacodes.firebaseapp.profile.component.RevokeAccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToProfileScreen: () -> Unit
) {
    val TAG = "VerifyEmailScreen"
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
// TODO: add downloadable fonts logic
    Scaffold(
        topBar = {
            TopBar(
                title = VERIFY_EMAIL_SCREEN,
                signOut = {
                    viewModel.signOut()
                },
                revokeAccess = {
                    viewModel.revokeAccess()
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
            VerifyEmailScreenContent(
                padding = padding,
                reloadUser = {
                    viewModel.reloadUser()
                }
            )
        }
    )

    ReloadUser(
        navigateToProfileScreen = {
            if (viewModel.isEmailVerified) {
                navigateToProfileScreen()
            } else {
                showMessage(context, EMAIL_NOT_VERIFIED_MESSAGE)
            }
        }
    )

    RevokeAccess(
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope,
        signOut = {
            viewModel.signOut()
        }
    )


}