package com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdacodes.firebaseapp.core.Constants.RESET_PASSWORD_BUTTON
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.ForgotPasswordFormState
import com.jdacodes.firebaseapp.feature_auth.presentation.forgot_password.ForgotPasswordState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordContent(
    usernameState: String,
    state: ForgotPasswordState,
    formState: ForgotPasswordFormState,
    onUserNameTextChange: (String) -> Unit,
    padding: PaddingValues,
    onClickForgotPassword: () -> Unit
) {

    val outlineTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
        cursorColor = MaterialTheme.colorScheme.primary,
        errorBorderColor = MaterialTheme.colorScheme.error,
        selectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.primary
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(64.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = usernameState,
            onValueChange = {
                onUserNameTextChange(it)
            },
            label = {
                Text(
                    text = "Username",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            maxLines = 1,
            singleLine = true,
            isError = formState.emailError != null,
            colors = outlineTextFieldColors
        )
        if (formState.emailError != null) {
            Text(
                text = formState.emailError ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick =
            onClickForgotPassword,
            shape = CircleShape,
            enabled = !state.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )

        ) {
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(12.dp),
                text = RESET_PASSWORD_BUTTON,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}