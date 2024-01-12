package com.jdacodes.firebaseapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.jdacodes.firebaseapp.core.Constants.CHAT_SCREEN
import com.jdacodes.firebaseapp.core.Constants.FORGOT_PASSWORD_SCREEN
import com.jdacodes.firebaseapp.core.Constants.PROFILE_SCREEN
import com.jdacodes.firebaseapp.core.Constants.SIGN_IN_SCREEN
import com.jdacodes.firebaseapp.core.Constants.SIGN_UP_SCREEN
import com.jdacodes.firebaseapp.core.Constants.USER_LIST_SCREEN
import com.jdacodes.firebaseapp.core.Constants.VERIFY_EMAIL_SCREEN

sealed class Screen(
    val route: String,
    var arguments: String? = "",
    var title: String? = "",
    var icon: ImageVector? = null
) {

    object SignInScreen : Screen(SIGN_IN_SCREEN)
    object ForgotPasswordScreen : Screen(FORGOT_PASSWORD_SCREEN)
    object SignUpScreen : Screen(SIGN_UP_SCREEN)
    object VerifyEmailScreen : Screen(VERIFY_EMAIL_SCREEN)
    object ProfileScreen : Screen(
        route = PROFILE_SCREEN,
        title = "Profile",
        icon = Icons.Filled.Person
    )

    object UserListScreen : Screen(
        route = USER_LIST_SCREEN,
        title = "Chat",
        icon = Icons.Filled.Chat,
        arguments = ""
    ) {
        val fullRoute = route + arguments
    }

    object ChatScreen : Screen(
        route = CHAT_SCREEN,
        arguments = "/{chatroomUUID}" + "/{opponentUUID}" + "/{registerUUID}" + "/{oneSignalUserId}",
        title = "Chat",
        icon = Icons.Filled.Chat,
    ) {
        val fullRoute = route + arguments
    }


}
