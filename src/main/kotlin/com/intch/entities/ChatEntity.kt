package com.intch.entities

import org.jetbrains.exposed.sql.Table

data class ChatEntity(
    val id: Int,
    val chatUuid: String,
    val chatCreated: String,
    val chatAllowedUsers: String
)

object ChatEntitySchema : Table() {
    val id = integer("id").autoIncrement()
    val chatUuid = varchar("chat_uuid", 50).uniqueIndex()
    val chatCreated = varchar("chat_created", 50)
    val chatAllowedUsers = varchar("chat_allowed_users", 250)

    override val primaryKey = PrimaryKey(UserEntitySchema.id)
}