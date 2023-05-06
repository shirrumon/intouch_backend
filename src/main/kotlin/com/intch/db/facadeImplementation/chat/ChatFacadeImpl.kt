package com.intch.db.facadeImplementation.chat

import com.intch.db.dao.DataBaseDAO.dbQuery
import com.intch.db.facade.chat.ChatFacadeDAO
import com.intch.entities.ChatEntity
import com.intch.entities.ChatEntitySchema
import com.intch.entities.UserEntitySchema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChatFacadeImpl : ChatFacadeDAO {
    private fun resultRowToChat(row: ResultRow) = ChatEntity(
        id = row[ChatEntitySchema.id],
        chatUuid = row[ChatEntitySchema.chatUuid],
        chatCreated = row[ChatEntitySchema.chatCreated],
        chatAllowedUsers = row[ChatEntitySchema.chatAllowedUsers]
    )
    override suspend fun allUserChats(user: Int): List<ChatEntity> {
        return ChatEntitySchema.selectAll().map(::resultRowToChat)
    }

    override suspend fun chat(id: String): ChatEntity? = dbQuery {
        ChatEntitySchema
            .select{ ChatEntitySchema.chatUuid eq id }
            .map(::resultRowToChat)
            .firstOrNull()
    }

    override suspend fun createNewChat(currentUser: String, targetUser: String): ChatEntity? = dbQuery {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentDateTime = LocalDateTime.now().format(formatter)

        val insertStatement = ChatEntitySchema.insert {
            it[chatUuid] = UUID.randomUUID().toString()
            it[chatCreated] = currentDateTime.toString()
            it[chatAllowedUsers] = "$currentUser|$targetUser"
        }

        insertStatement.resultedValues?.firstOrNull()?.let(::resultRowToChat)
    }

    override suspend fun deleteChat(id: String): Boolean = dbQuery {
        UserEntitySchema.deleteWhere { ChatEntitySchema.chatUuid eq id } > 0
    }
}