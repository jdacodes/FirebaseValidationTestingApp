package com.jdacodes.firebaseapp.feature_chat.presentation.userlist.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogCustomOutlinedTextField(
    entry: String,
    hint: String,
    onChange:(String) -> Unit = {},
    onFocusChange:(Boolean) -> Unit = {}){

    var isNameChange by remember { mutableStateOf(false) }
    var isFocusChange by remember { mutableStateOf(false) }


    var text by remember { mutableStateOf("") }
    text = entry

    val fullWidthModifier = Modifier
        .fillMaxWidth()
        .onFocusChanged {
            if (isNameChange) {
                isFocusChange = true
                onFocusChange(isFocusChange)
            }
        }

    OutlinedTextField(
        modifier = fullWidthModifier,
        singleLine = true,
        value = text,
        label = { Text(hint) },
        onValueChange = {
            text = it
            onChange(it)
            isNameChange = true
        })
}