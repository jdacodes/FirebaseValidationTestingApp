package com.jdacodes.firebaseapp.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jdacodes.firebaseapp.core.Constants.PROFILE_SCREEN
import com.jdacodes.firebaseapp.core.Constants.SIGN_OUT_MESSAGE
import com.jdacodes.firebaseapp.core.components.TopBar
import com.jdacodes.firebaseapp.feature_auth.presentation.UserData
import com.jdacodes.firebaseapp.profile.component.ProfileScreenContent
import com.jdacodes.firebaseapp.profile.component.RevokeAccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userData: UserData?,
//    onSignOut: () -> Unit
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val TAG = "ProfileScreen"
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBar(
                title = PROFILE_SCREEN,
                signOut = {
                    viewModel.signOut()
                    Toast.makeText(
                        context,
                        SIGN_OUT_MESSAGE,
                        Toast.LENGTH_LONG
                    ).show()
                },
                revokeAccess = {
                    viewModel.revokeAccess()
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
            ProfileScreenContent(
                userData = userData,
                padding = padding
            )
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