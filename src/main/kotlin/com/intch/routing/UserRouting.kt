package com.intch.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.intch.models.UserModel
import com.intch.services.user.UserAuthService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureUserRouting() {
    routing {
        post("/login") {
            val user = call.receive<UserModel>()
            UserAuthService().checkUserWhenLogin(user).collect { isUserExistAndChecked ->
                if(isUserExistAndChecked) {
                    val token = JWT.create()
                        .withAudience(this@configureUserRouting.environment.config.property("jwt.audience").getString())
                        .withIssuer(this@configureUserRouting.environment.config.property("jwt.domain").getString())
                        .withClaim("username", user.username)
                        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                        .sign(Algorithm.HMAC256("secret"))
                    call.respond(hashMapOf("token" to token))
                } else {
                    call.respond("User not exist or password/login is incorrect")
                }
            }
        }

        post("/register") {
            val user = call.receive<UserModel>()
            UserAuthService().registerUser(user)
            call.respond("Successfully registered")
        }
    }
}