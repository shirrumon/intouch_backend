package com.intch.models.chat

import io.ktor.websocket.*

class Connection(val session: DefaultWebSocketSession) {
    var userId: String = ""
    var chatId: String? = null
    val name = "user${userId}"
}