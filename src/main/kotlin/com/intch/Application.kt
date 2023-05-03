package com.intch

import com.intch.db.dao.DataBaseDAO
import io.ktor.server.application.*
import com.intch.plugins.*
import com.intch.routing.configureUserRouting

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureSecurity()
    configureSockets()
    configureUserRouting()
    configureSerialization()
    DataBaseDAO.init()
    configureRouting()
}
