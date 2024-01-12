package com.jdacodes.firebaseapp.navigation

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
import com.jdacodes.firebaseapp.feature_chat.presentation.chat.ChatScreen
import com.jdacodes.firebaseapp.feature_chat.presentation.userlist.Userlist
import com.jdacodes.firebaseapp.profile.ProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController
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
            ProfileScreen(userData = googleAuthUiClient.getSignedInUser(),
                navigateProfileScreen = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(
            route = Screen.SignUpScreen.route
        ) {
            val viewModel: SignUpViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val formState = viewModel.formState

            SignUpScreen(
                navigateBack = {
                    navController.popBackStack()
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

        composable(
//            BottomNavItem.UserList.fullRoute,
            Screen.UserListScreen.fullRoute,
//            enterTransition = {
//                when (initialState.destination.route) {
//                    Screen.UserListScreen.fullRoute ->
//                        slideIntoContainer(
//                            AnimatedContentScope.SlideDirection.Left,
//                            animationSpec = tween(250, easing = LinearEasing)
//                        )
//                    BottomNavItem.SignIn.fullRoute ->
//                        slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))
//                    BottomNavItem.SignUp.fullRoute ->
//                        slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))
//                    BottomNavItem.Profile.fullRoute ->
//                        slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))

//                    else -> null
//                }

//            }, exitTransition = {
//                when (targetState.destination.route) {
//                    Screen.UserListScreen.fullRoute ->
//                        slideOutOfContainer(
//                            AnimatedContentScope.SlideDirection.Right,
//                            animationSpec = tween(250, easing = LinearEasing)
//                        )
//                    else -> null
//                }
//            }
        ) {
            Userlist(
                navController = navController,
                snackbarHostState = snackbarHostState,
                keyboardController = keyboardController
            )
        }

        composable(
            Screen.ChatScreen.fullRoute,
            arguments = listOf(
                navArgument("chatroomUUID") {
                    type = NavType.StringType
                }, navArgument("opponentUUID") {
                    type = NavType.StringType
                }, navArgument("registerUUID") {
                    type = NavType.StringType
                }, navArgument("oneSignalUserId") {
                    type = NavType.StringType
                })
        ) {
            val chatroomUUID = remember {
                it.arguments?.getString("chatroomUUID")
            }
            val opponentUUID = remember {
                it.arguments?.getString("opponentUUID")
            }
            val registerUUID = remember {
                it.arguments?.getString("registerUUID")
            }
            val oneSignalUserId = remember {
                it.arguments?.getString("oneSignalUserId")
            }

            ChatScreen(
                chatRoomUUID = chatroomUUID ?: "",
                opponentUUID = opponentUUID ?: "",
                registerUUID = registerUUID ?: "",
                oneSignalUserId = oneSignalUserId ?: "",
                navController = navController,
                snackbarHostState = snackbarHostState,
                keyboardController = keyboardController
            )
        }
    }
}