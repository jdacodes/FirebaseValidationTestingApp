package com.jdacodes.firebaseapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.jdacodes.firebaseapp.profile.ProfileScreen
import com.jdacodes.firebaseapp.sign_in.presentation.GoogleAuthUiClient
import com.jdacodes.firebaseapp.sign_in.presentation.SignInFormEvent
import com.jdacodes.firebaseapp.sign_in.presentation.SignInScreen
import com.jdacodes.firebaseapp.sign_in.presentation.SignInViewModel
import com.jdacodes.firebaseapp.ui.theme.FirebaseAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "sign_in") {
                        //Composable block with sign_in route
                        composable("sign_in") {
                            val viewModel = viewModel<SignInViewModel>()
                            val signInState by viewModel.state.collectAsStateWithLifecycle()
                            val signInFormState = viewModel.stateForm
                            //Check if the user is already signed in
                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthUiClient.getSignedInUser() != null) {
                                    navController.navigate("profile")
                                }
                            }

                            //Send the intent we get from intentSender
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            //Check for Sign in Validation event is successful
                            val context = LocalContext.current
                            LaunchedEffect(key1 = context) {
                                viewModel.validationEvents.collect { event ->
                                    when (event) {
                                        is SignInViewModel.ValidationEvent.Success -> {
//                                            Toast.makeText(
//                                                context,
//                                                "Registration successful",
//                                                Toast.LENGTH_LONG
//                                            ).show()

                                        }
                                    }
                                }
                            }
                            //Listen if the sign-in process is success or not
                            LaunchedEffect(key1 = signInState.isSignInSuccessful) {
                                if (signInState.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("profile")
                                    viewModel.resetState()
                                }
                            }

                            //Sign in screen for the sign_in composable
                            SignInScreen(
                                signInState = signInState,
                                signInFormState = signInFormState,
                                onClickSignInGoogle = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                },
                                viewModel = viewModel,
                                onClickSignInEmailAndPassword = {
                                    lifecycleScope.launch {
                                        //trigger validation
                                        viewModel.onEvent(SignInFormEvent.Submit)

                                        Log.d(
                                            "inside_onClickSignInEmailAndPassword",
                                            signInFormState.email.trim()
                                        )
                                        Log.d(
                                            "inside_onClickSignInEmailAndPassword",
                                            signInFormState.password.trim()
                                        )

                                        val signInResult =
                                            googleAuthUiClient.signInWithEmailAndPassword(
                                                signInFormState.email.trim(),
                                                signInFormState.password.trim(),
//                                                "johndoe@example.com", "test123"
                                            )
                                        Log.d(
                                            "inside_onClickSignInEmailAndPassword",
                                            "$signInResult"
                                        )
                                        viewModel.onSignInResult(signInResult)
                                    }
                                },
                            )
                        }

                        //Composable block with profile route
                        composable("profile") {
                            ProfileScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Sign out",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.popBackStack()
                                    }
                                }
                            )
                        }

                    }
                }
            }
        }
    }
}
