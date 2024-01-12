package com.jdacodes.firebaseapp.feature_chat.domain.use_case.chat_screen

import com.jdacodes.firebaseapp.feature_chat.domain.repository.ChatScreenRepository

class LoadOpponentProfileFromFirebase(
    private val chatScreenRepository: ChatScreenRepository
) {
    suspend operator fun invoke(opponentUUID: String) =
        chatScreenRepository.loadOpponentProfileFromFirebase(opponentUUID)
}