package com.intch.plugins

import com.intch.models.chat.Connection
import io.ktor.network.sockets.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.LinkedHashSet

fun Application.configureSockets() {
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
            connections += thisConnection
            sessionsById[thisConnection.userId] = thisConnection

            val chatId = call.request.headers["chat_id"]
            val targetUserId = call.request.headers["target_user_id"]

            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
                    if(chatId == "1") {
                        sessionsById[targetUserId?.toInt()]?.session?.send(textWithUsername)
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
