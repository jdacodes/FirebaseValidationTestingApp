package com.jdacodes.firebaseapp.core

object Constants {
    //App
    const val TAG = "AppTag"

    //Screens
    const val SIGN_IN_SCREEN = "Sign in"
    const val FORGOT_PASSWORD_SCREEN = "Forgot password"
    const val SIGN_UP_SCREEN = "Sign up"
    const val VERIFY_EMAIL_SCREEN = "Verify email"
    const val PROFILE_SCREEN = "Profile"

    //Useful
    const val EMPTY_STRING = ""

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
    const val FONT_SUCCESSFUL_MESSAGE = "Provider and Certificates for Font available"
    const val FONT_FAILURE_MESSAGE = "There has been an issue: "
    const val EMAIL_BLANK_ERROR_MESSAGE = "The email can't be blank"
    const val EMAIL_INVALID_ERROR_MESSAGE = "That's not a valid email"
    const val PASSWORD_MINIMUM_CHARACTER_ERROR_MESSAGE = "The password needs to consist of at least 8 characters"
    const val PASSWORD_LETTER_DIGIT_ERROR_MESSAGE = "The password needs to contain at least one letter and digit"
    const val PASSWORD_NOT_MATCH_ERROR_MESSAGE = "The passwords don't match"
    const val TERMS_NOT_ACCEPTED_ERROR_MESSAGE = "Please accept the terms"

}