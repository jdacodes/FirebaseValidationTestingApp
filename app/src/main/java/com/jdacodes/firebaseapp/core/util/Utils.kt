package com.jdacodes.firebaseapp.core.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.jdacodes.firebaseapp.core.Constants.TAG

class Utils {
    companion object {
        fun print(e: Exception) = Log.e(TAG, e.stackTraceToString())

        fun showMessage(
            context: Context,
            message: String?
        ) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}