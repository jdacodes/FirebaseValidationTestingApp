package com.jdacodes.firebaseapp.profile.component

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jdacodes.firebaseapp.core.Constants.ACCESS_REVOKED_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.REVOKE_ACCESS_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.SENSITIVE_OPERATION_MESSAGE
import com.jdacodes.firebaseapp.core.Constants.SIGN_OUT_ITEM
import com.jdacodes.firebaseapp.core.components.ProgressBar
import com.jdacodes.firebaseapp.core.util.Response
import com.jdacodes.firebaseapp.core.util.Utils.Companion.showMessage
import com.jdacodes.firebaseapp.profile.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RevokeAccess(
    viewModel: ProfileViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    signOut: () -> Unit,
) {
    val context = LocalContext.current

    fun showRevokeAccessMessage() = coroutineScope.launch {
        val result = snackbarHostState.showSnackbar(
            message = REVOKE_ACCESS_MESSAGE,
            actionLabel = SIGN_OUT_ITEM
        )
        if (result == SnackbarResult.ActionPerformed) {
            signOut()
        }
    }

    when(val revokeAccessResponse = viewModel.revokeAccessResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> {
            val isAccessRevoked = revokeAccessResponse.data
            LaunchedEffect(isAccessRevoked) {
                if (isAccessRevoked) {
                    showMessage(context, ACCESS_REVOKED_MESSAGE)
                }
            }
        }
        is Response.Failure -> revokeAccessResponse.apply {
            LaunchedEffect(e) {
                print(e)
                if (e.message == SENSITIVE_OPERATION_MESSAGE) {
                    showRevokeAccessMessage()
                }
            }
        }

        else -> {}
    }
}