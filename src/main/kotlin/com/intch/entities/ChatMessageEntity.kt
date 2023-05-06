package com.intch.entities

import org.jetbrains.exposed.sql.Table

data class ChatMessageEntity(
    val id: Int,
    val messageText: String,
    val fromChat: String,
    val userFrom: String,
    val userTo: String,
    val isRead: Boolean,
    val sentTimestamp: String
)

object ChatMessageSchema : Table() {
    val id = integer("id").autoIncrement()
    val messageText = text("message_text")
    val fromChat = varchar("from_chat", 50)
    val userFrom = varchar("user_from", 50)
    val userTo = varchar("user_to", 50)
    val isRead = bool("is_read")
    val sentTimestamp = varchar("sent_time_stamp", 50)

    override val primaryKey = PrimaryKey(UserEntitySchema.id)
}