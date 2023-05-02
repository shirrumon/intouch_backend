package com.intch

import io.ktor.server.application.*
import com.intch.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureSecurity()
    configureSockets()
    configureUserRouting()
    configureRouting()
    configureSerialization()
}
