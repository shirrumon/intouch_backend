package com.intch.entities

import org.jetbrains.exposed.sql.*

data class UserEntity(
    val id: Int,
    val username: String,
    val password: String
)

object UserEntitySchema : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 128)
    val password = varchar("password", 1024)

    override val primaryKey = PrimaryKey(id)
}