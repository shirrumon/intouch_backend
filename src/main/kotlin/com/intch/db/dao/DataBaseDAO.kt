package com.intch.db.dao

import com.intch.entities.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DataBaseDAO {
    fun init() {
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = "jdbc:postgresql://127.0.0.1:54333/intouch?user=postgres"
        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            SchemaUtils.create(UserEntitySchema)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}