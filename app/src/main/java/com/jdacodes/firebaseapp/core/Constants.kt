package com.jdacodes.firebaseapp.core

object Constants {
    //App
    const val TAG = "AppTag"

    //Buttons
    const val SIGN_IN_BUTTON = "Sign in"
    const val RESET_PASSWORD_BUTTON = "Reset"
    const val SIGN_UP_BUTTON = "Sign up"

    //Menu Items
    const val SIGN_OUT_ITEM = "Sign out"
    const val REVOKE_ACCESS_ITEM = "Revoke Access"

    //Screens
    const val SIGN_IN_SCREEN = "Sign in"
    const val FORGOT_PASSWORD_SCREEN = "Forgot password"
    const val SIGN_UP_SCREEN = "Sign up"
    const val VERIFY_EMAIL_SCREEN = "Verify email"
    const val PROFILE_SCREEN = "Profile"
    const val USER_LIST_SCREEN = "User list"
    const val CHAT_SCREEN = "Chat"

    //Labels
    const val EMAIL_LABEL = "Email"
    const val PASSWORD_LABEL = "Password"

    //Useful
    const val EMPTY_STRING = ""

    //Texts
    const val FORGOT_PASSWORD = "Forgot password?"
    const val NO_ACCOUNT = "No account? Sign up."
    const val ALREADY_USER = "Already a user? Sign in."
    const val WELCOME_MESSAGE = "Welcome to our app."
    const val ALREADY_VERIFIED = "Already verified?"
    const val SPAM_EMAIL = "If not, please also check the spam folder."

    //Messages
    const val VERIFY_EMAIL_MESSAGE = "We've sent you an email with a link to verify the email."
    const val EMAIL_NOT_VERIFIED_MESSAGE = "Your email is not verified."
    const val RESET_PASSWORD_MESSAGE = "We've sent you an email with a link to reset the password."
    const val REVOKE_ACCESS_MESSAGE = "You need to re-authenticate before revoking the access."
    const val ACCESS_REVOKED_MESSAGE = "Your access has been revoked."
    const val SIGNUP_SUCCESSFUL_MESSAGE = "Sign up successful! You can now Sign in"
    const val SIGNUP_FAILURE_MESSAGE = "Sign up failure.Please try again"
    const val SIGNIN_SUCCESSFUL_MESSAGE = "Sign in successful! Welcome back"
    const val SIGNIN_FAILURE_MESSAGE = "Sign in failure. Please try again"
    const val FORGOT_PASSWORD_FAILURE_MESSAGE = "Sign in failure. Please try again"
    const val FONT_SUCCESSFUL_MESSAGE = "Provider and Certificates for Font available"
    const val FONT_FAILURE_MESSAGE = "There has been an issue: "
    const val EMAIL_BLANK_ERROR_MESSAGE = "The email can't be blank"
    const val EMAIL_INVALID_ERROR_MESSAGE = "That's not a valid email"
    const val PASSWORD_MINIMUM_CHARACTER_ERROR_MESSAGE = "The password needs to consist of at least 8 characters"
    const val PASSWORD_LETTER_DIGIT_ERROR_MESSAGE = "The password needs to contain at least one letter and digit"
    const val PASSWORD_NOT_MATCH_ERROR_MESSAGE = "The passwords don't match"
    const val TERMS_NOT_ACCEPTED_ERROR_MESSAGE = "Please accept the terms"
    const val SIGN_OUT_MESSAGE = "Signing out"

    //Error Messages
    const val SENSITIVE_OPERATION_MESSAGE = "This operation is sensitive and requires recent authentication. Log in again before retrying this request."

    //Chat Feature
    const val ERROR_MESSAGE = "Unexpected error."
    const val NO_CHATROOM_IN_FIREBASE_DATABASE = "NO_CHATROOM_IN_FIREBASE_DATABASE"
    const val ONESIGNAL_APP_ID = "YOUR_ONE_SIGNAL"
}