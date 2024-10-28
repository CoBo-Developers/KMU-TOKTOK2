package cobo.chat.repository.custom

import cobo.chat.data.entity.Chat
import cobo.chat.data.entity.ChatRoom

interface ChatRepositoryCustom {
    fun insert(chat: Chat)
    fun findByChatRoomWithJDBC(chatRoom: ChatRoom): List<Chat>
    fun findByChatRoomAndUpdateWithJDBC(chatRoom: ChatRoom): List<Chat>
}