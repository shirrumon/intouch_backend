package com.intch.routing.chat

import com.intch.models.chat.Connection
import com.intch.services.chat.ChatService
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.LinkedHashSet

fun Application.configureSockets() {
    val chatService = ChatService()

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        authenticate {
            val connections = Collections.synchronizedSet<Connection>(LinkedHashSet())
            val sessionsById = ConcurrentHashMap<String, Connection>()
            webSocket("/chat") {
                val thisConnection = Connection(this)
                val principal = call.principal<JWTPrincipal>()
                val thisUserId = principal!!.payload.getClaim("user").asString()
                val targetChatId = call.request.headers["chat_id"]
                val targetUserId = call.request.headers["target_user_id"]

                thisConnection.chatId = targetChatId
                thisConnection.userId = thisUserId

                connections += thisConnection
                sessionsById[thisUserId] = thisConnection

                try {
                    send("You are connected! There are ${connections.count()} users here.")
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()

                        if(sessionsById.containsKey(targetUserId)) {
                            sessionsById[targetUserId]?.session?.send(receivedText)
                        }

                        chatService.createMessage(
                            receivedText,
                            thisUserId!!,
                            targetUserId!!,
                            targetChatId
                        )
                    }
                } catch (e: Exception) {
                    println(e.message)
                } finally {
                    sessionsById.remove(thisConnection.userId)
                    connections -= thisConnection
                }
            }
        }
    }
}
