package com.intch.services.chat

import com.intch.db.facadeImplementation.chat.ChatFacadeImpl
import com.intch.db.facadeImplementation.chat.ChatMessageImpl

class ChatService {
    private val chatImplementation = ChatFacadeImpl()
    private val messageFacadeImplementation = ChatMessageImpl()
    suspend fun createMessage(
        messageText: String,
        userFrom: Int,
        userTo: Int,
        toChat: String? = null
    ) {
        if(toChat == null) {
            val chat = chatImplementation.createNewChat(userFrom, userTo)
            messageFacadeImplementation.createNewMessage(
                messageText,
                chat!!.chatUuid,
                userFrom,
                userTo
            )
        } else {
            messageFacadeImplementation.createNewMessage(
                messageText,
                toChat,
                userFrom,
                userTo
            )
        }
    }
}