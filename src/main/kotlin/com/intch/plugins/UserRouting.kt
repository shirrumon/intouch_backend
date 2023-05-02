package com.intch.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.intch.entities.UserEntity
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureUserRouting() {
    routing {
        post("/login") {
            val user = call.receive<UserEntity>()
            // Check username and password
            // ...
            val token = JWT.create()
                .withAudience(this@configureUserRouting.environment.config.property("jwt.audience").getString())
                .withIssuer(this@configureUserRouting.environment.config.property("jwt.domain").getString())
                .withClaim("username", user.userName)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256("secret"))
            call.respond(hashMapOf("token" to token))
        }
    }
}