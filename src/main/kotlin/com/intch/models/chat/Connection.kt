package com.intch.models.chat

import io.ktor.websocket.*
import java.util.concurrent.atomic.*

class Connection(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val userId = lastId.getAndIncrement()
    val name = "user${userId}"
}