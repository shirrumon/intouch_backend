package com.intch.db.facade.chat

import com.intch.entities.ChatEntity

interface ChatFacadeDAO {
    suspend fun allUserChats(user: Int): List<ChatEntity>
    suspend fun chat(id: String): ChatEntity?
    suspend fun createNewChat(currentUser: Int, targetUser: Int): ChatEntity?
    suspend fun deleteChat(id: String): Boolean
}