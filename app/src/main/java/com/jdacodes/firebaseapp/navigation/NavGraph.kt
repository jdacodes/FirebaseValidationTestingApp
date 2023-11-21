package com.jdacodes.firebaseapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jdacodes.firebaseapp.feature_auth.presentation.sign_up.SignUpScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignInScreen.route
    ) {
        composable(
            route = Screen.SignUpScreen.route
        ) {
            SignUpScreen(
                navigateBack = {
                    navController.popBackStack()
                },
//                state = SignUpFormState(
//
//                ),
//                onClickSignUp = {},
//                viewModel = SignUpViewModel(
//                    validateEmail = ValidateEmail(),
//                    validatePassword = ValidatePassword(),
//                    validateRepeatedPassword = ValidateRepeatedPassword(),
//                    validateTerms = ValidateTerms()
//                ),
// TODO: remove state, onClickSignUp and viewModel
            )
        }
    }
}