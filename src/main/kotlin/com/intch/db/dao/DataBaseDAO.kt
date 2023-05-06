package com.intch.db.dao

import com.intch.entities.*
import io.ktor.server.config.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DataBaseDAO {
    fun init(config: ApplicationConfig) {
        val database = Database.connect(
            config.property("storage.jdbcURL").getString(),
            config.property("storage.driverClassName").getString()
        )

        transaction(database) {
            SchemaUtils.create(UserEntitySchema)
            SchemaUtils.create(ChatEntitySchema)
            SchemaUtils.create(ChatMessageSchema)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}