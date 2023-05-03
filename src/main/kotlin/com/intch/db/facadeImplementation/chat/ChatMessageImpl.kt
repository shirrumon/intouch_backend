package com.intch.db.facadeImplementation.chat

import com.intch.db.dao.DataBaseDAO.dbQuery
import com.intch.db.facade.chat.ChatMessageFacadeDAO
import com.intch.entities.ChatMessageEntity
import com.intch.entities.ChatMessageSchema
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME

class ChatMessageImpl : ChatMessageFacadeDAO {
    private fun resultRowToChatMessage(row: ResultRow) = ChatMessageEntity(
        id = row[ChatMessageSchema.id],
        messageText = row[ChatMessageSchema.messageText],
        fromChat = row[ChatMessageSchema.fromChat],
        userFrom = row[ChatMessageSchema.userFrom],
        userTo = row[ChatMessageSchema.userTo],
        isRead = row[ChatMessageSchema.isRead],
        sentTimestamp = row[ChatMessageSchema.sentTimestamp]
    )

    override suspend fun allMessagesInChat(chatId: String): List<ChatMessageEntity> {
        return ChatMessageSchema.selectAll().map(::resultRowToChatMessage)
    }

    override suspend fun createNewMessage(
        messageText: String,
        fromChat: String,
        userFrom: Int,
        userTo: Int
    ): ChatMessageEntity? = dbQuery {
        val formatter = ISO_OFFSET_DATE_TIME
        val currentDateTime = LocalDateTime.now().format(formatter)

        val inserted = ChatMessageSchema.insert {
            it[ChatMessageSchema.messageText] = messageText
            it[ChatMessageSchema.fromChat] = fromChat
            it[ChatMessageSchema.userFrom] = userFrom
            it[ChatMessageSchema.userTo] = userTo
            it[isRead] = false
            it[sentTimestamp] = currentDateTime.toString()
        }

        inserted.resultedValues?.firstOrNull()?.let(::resultRowToChatMessage)
    }

    override suspend fun deleteMessage(id: Int): Boolean = dbQuery {
        ChatMessageSchema.deleteWhere { ChatMessageSchema.id eq id } > 0
    }
}