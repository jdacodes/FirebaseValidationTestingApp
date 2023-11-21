package com.jdacodes.firebaseapp.feature_auth.presentation

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jdacodes.firebaseapp.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun FacebookButton(
    onAuthComplete: (SignInResult) -> Unit,
    onAuthError: (SignInResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val launcher = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {
        // nothing to do. handled in FacebookCallback
    }
    val containerModifier = Modifier.size(48.dp)
    val iconModifier = Modifier.size(24.dp)
    val backgroundColor = Color(0xFFB9B9B9).copy(alpha = 0.2f)
    val borderColor = Color(0xFFB6B6B6)
    val iconColor = Color(0xFF1877f2)

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                // do nothing
            }

            override fun onError(error: FacebookException) {

                val result = SignInResult(
                    data = null,
                    errorMessage = error.message.toString()
                )
                onAuthError(result)
            }

            override fun onSuccess(result: LoginResult) {
                scope.launch {
                    val token = result.accessToken.token
                    val credential = FacebookAuthProvider.getCredential(token)
                    val user = Firebase.auth.signInWithCredential(credential).await().user
                    if (user != null) {
                        // TODO: Displays generic photo from user 
                        var photoUrl: Uri? = null
                        user.apply {
                            for (userInfo in providerData) {
                                if (userInfo.providerId == "facebook.com") {
                                    photoUrl = userInfo.photoUrl
                                    Log.d("facebook photo", photoUrl.toString())
                                }
                            }
                        }

                        val signInResult = SignInResult(
                            data = user.run {
                                UserData(
                                    userId = uid,
                                    username = displayName,
                                    profilePictureUrl = photoUrl?.toString()
                                )

                            },
                            errorMessage = null
                        )
                        user.run {
                            Log.d("facebook photo", photoUrl.toString())
                        }

                        onAuthComplete(signInResult)

                    } else {
                        val signInResult = SignInResult(
                            data = null,
                            errorMessage = "Unable to sign in with Facebook"
                        )
                        onAuthError(signInResult)
                    }
                }
            }

        })

        onDispose {
            loginManager.unregisterCallback(callbackManager)
        }
    }

    OutlinedIconButton(
        onClick = {// start the sign-in flow
            launcher.launch(listOf("email", "public_profile"))
        },
        colors = IconButtonDefaults.outlinedIconButtonColors(
            containerColor = backgroundColor,
            contentColor = iconColor
        ),
        border = BorderStroke(width = 2.dp, color = borderColor),
        modifier = containerModifier
    ) {
        Icon(
            modifier = iconModifier,
            painter = painterResource(id = R.drawable.ic_facebook_round),
//            tint = Color.Unspecified,
            contentDescription = "Google"
        )
    }
}