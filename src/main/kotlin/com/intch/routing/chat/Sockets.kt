package com.intch.routing.chat

import com.intch.models.chat.Connection
import com.intch.services.chat.ChatService
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
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
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        val sessionsById = ConcurrentHashMap<Int, Connection>()
        webSocket("/chat") { // websocketSession
            println("Adding user!")
            val thisConnection = Connection(this)

            val targetChatId = call.request.headers["chat_id"]
            val targetUserId = call.request.headers["target_user_id"]

            thisConnection.chatId = targetChatId

            val targetUserChatId = sessionsById[targetUserId?.toInt()]?.chatId

            connections += thisConnection
            sessionsById[thisConnection.userId] = thisConnection

            val targetUserFromSession = sessionsById[targetUserId?.toInt()]

            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val textWithUsername = "[${thisConnection.name}]: $receivedText"

                    if(targetUserFromSession == null) {
                        chatService.createMessage(
                            receivedText,
                            thisConnection.userId,
                            targetUserId!!.toInt(),
                            targetChatId
                        )
                    } else {
                        if(targetUserChatId?.equals(targetChatId) == true) {
                            chatService.createMessage(
                                receivedText,
                                thisConnection.userId,
                                targetUserFromSession.userId,
                                targetChatId
                            )

                            targetUserFromSession.session.send(textWithUsername)
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }
    }
}
