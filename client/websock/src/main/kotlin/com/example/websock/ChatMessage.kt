package com.example.websock

data class ChatMessage(
        var type: MessageType,
        var content: String?,
        var sender: String
)