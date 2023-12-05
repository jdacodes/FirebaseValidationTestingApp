package com.jdacodes.firebaseapp.navigation

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jdacodes.firebaseapp.core.Constants
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.ForgotPasswordFormEvent
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.ForgotPasswordScreen
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.ForgotPasswordViewModel
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.GoogleAuthUiClient
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.SignInFormEvent
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.SignInScreen
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_in.SignInViewModel
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_up.SignUpScreen
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_up.SignUpViewModel
import com.jdacodes.firebaseapp.feature_auth.presentation.verify_email.VerifyEmailScreen
import com.jdacodes.firebaseapp.profile.ProfileScreen
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignInScreen.route
    ) {

        composable(
            route = Screen.SignInScreen.route
        ) {
            val viewModel: SignInViewModel = hiltViewModel()
            val signInState by viewModel.state.collectAsStateWithLifecycle()
            val formState = viewModel.formState

            //Send the intent we get from intentSender
            val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == ComponentActivity.RESULT_OK) {
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
                            viewModel.updateSignInState()

//                                            Toast.makeText(
//                                                context,
//                                                "Registration successful",
//                                                Toast.LENGTH_LONG
//                                            ).show()

                        }

                        else -> {}
                    }
                }
            }

            //Listen if the sign-in process is success or not
            LaunchedEffect(key1 = signInState.isSignInSuccessful) {
                if (signInState.isSignInSuccessful) {
                    Toast.makeText(
                        context,
                        Constants.SIGNIN_SUCCESSFUL_MESSAGE,
                        Toast.LENGTH_LONG
                    ).show()

//                    navController.navigate(Screen.ProfileScreen.route)
                    viewModel.resetState()
                }
            }

            //Sign in screen for the sign_in composable
            SignInScreen(
                signInState = signInState,
                signInFormState = formState,
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
                    viewModel.onEvent(SignInFormEvent.Submit)
                },
                onClickDontHaveAccount = {
                    navController.navigate(Screen.SignUpScreen.route)
                },
                onClickForgotPassword = {
                    navController.navigate(Screen.ForgotPasswordScreen.route)
                }
            )
        }

        composable(
            route = Screen.ProfileScreen.route
        ) {
            ProfileScreen(userData = googleAuthUiClient.getSignedInUser())
        }

        composable(
            route = Screen.SignUpScreen.route
        ) {
            val viewModel: SignUpViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val formState = viewModel.formState

//            val context = LocalContext.current
//            LaunchedEffect(key1 = context) {
//                viewModel.validationEvents.collect { event ->
//                    when (event) {
//                        is SignUpViewModel.ValidationEvent.Success -> {
//                            viewModel.updateSignUpState()

//                                            Toast.makeText(
//                                                context,
//                                                "Registration successful",
//                                                Toast.LENGTH_LONG
//                                            ).show()

//                        }
//
//                        else -> {}
//                    }
//                }
//            }

//            LaunchedEffect(key1 = state.isSignUpSuccessful) {
//                if (state.isSignUpSuccessful) {
//                    Toast.makeText(
//                        context,
//                        Constants.SIGNUP_SUCCESSFUL_MESSAGE,
//                        Toast.LENGTH_LONG
//                    ).show()

//                    navController.navigate(Screen.ProfileScreen.route)
//                    viewModel.resetState()
//                }
//            }
            // TODO: Fix issue navigating to profile after successful sign up
            SignUpScreen(
                navigateBack = {
                    navController.popBackStack()
//                    navController.navigate(Screen.SignInScreen.route) {
//                        popUpTo(navController.graph.id) {
//                            inclusive = true
//                        }
//                    }
                },
                state = state,
                formState = formState
            )
        }

        composable(
            route = Screen.VerifyEmailScreen.route
        ) {
            VerifyEmailScreen(
                navigateToProfileScreen = {
                    navController.navigate(Screen.ProfileScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }

                }
            )
        }

        composable(
            route = Screen.ForgotPasswordScreen.route
        ) {
            val viewModel: ForgotPasswordViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val formState = viewModel.formState
            ForgotPasswordScreen(
                navigateBack = {
                    navController.popBackStack()
                },
                state = state,
                formState = formState
            )
        }

    }
}