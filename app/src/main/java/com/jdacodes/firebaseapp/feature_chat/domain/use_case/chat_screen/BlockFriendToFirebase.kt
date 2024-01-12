package com.jdacodes.firebaseapp.feature_chat.domain.use_case.chat_screen

import com.jdacodes.firebaseapp.feature_chat.domain.repository.ChatScreenRepository

class BlockFriendToFirebase(
    private val chatScreenRepository: ChatScreenRepository
) {
    suspend operator fun invoke(registerUUID: String) =
        chatScreenRepository.blockFriendToFirebase(registerUUID)
}