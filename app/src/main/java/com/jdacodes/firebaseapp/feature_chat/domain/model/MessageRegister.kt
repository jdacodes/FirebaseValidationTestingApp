package com.jdacodes.firebaseapp.feature_chat.domain.model

data class MessageRegister(
    var chatMessage: ChatMessage,
    var isMessageFromOpponent: Boolean
)
