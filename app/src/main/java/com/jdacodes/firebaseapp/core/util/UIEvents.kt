package com.jdacodes.firebaseapp.core.util

sealed class UIEvents {
    data class SnackBarEvent(val message: String) : UIEvents()

}
