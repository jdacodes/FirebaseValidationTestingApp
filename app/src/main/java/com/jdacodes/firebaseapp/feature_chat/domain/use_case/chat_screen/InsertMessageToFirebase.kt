package com.jdacodes.firebaseapp.feature_chat.domain.use_case.chat_screen

import com.jdacodes.firebaseapp.feature_chat.domain.repository.ChatScreenRepository

class InsertMessageToFirebase(
    private val chatScreenRepository: ChatScreenRepository
) {
    suspend operator fun invoke(
        chatRoomUUID: String,
        messageContent: String,
        registerUUID: String,
        oneSignalUserId: String
    ) = chatScreenRepository.insertMessageToFirebase(
        chatRoomUUID,
        messageContent,
        registerUUID,
        oneSignalUserId
    )
}