package com.intch.db.facade.chat

import com.intch.entities.ChatMessageEntity

interface ChatMessageFacadeDAO {
    suspend fun allMessagesInChat(chatId: String): List<ChatMessageEntity>
    suspend fun createNewMessage(
        messageText: String,
        fromChat: String,
        userFrom: String,
        userTo: String
    ): ChatMessageEntity?
    suspend fun deleteMessage(id: Int): Boolean
}