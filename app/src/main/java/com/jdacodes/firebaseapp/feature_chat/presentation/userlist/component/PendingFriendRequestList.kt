package com.jdacodes.firebaseapp.feature_chat.presentation.userlist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.jdacodes.firebaseapp.feature_chat.domain.model.FriendListRegister
import com.jdacodes.firebaseapp.ui.theme.spacing

@Composable
fun PendingFriendRequestList(
    item: FriendListRegister,
    onAcceptClick: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.spacing.medium,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = androidx.compose.ui.Modifier.padding(MaterialTheme.spacing.small),
                text = item.requesterEmail,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Row() {
                TextButton(
                    onClick = { onCancelClick() }
                ) {
                    Text(text = "Decline", color = MaterialTheme.colorScheme.error)
                }
                TextButton(onClick = { onAcceptClick() }) {
                    Text(text = "Accept")
                }
            }

        }
    }
}