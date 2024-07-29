package cobo.chat.repository

import cobo.chat.data.entity.Chat
import cobo.chat.data.entity.ChatRoom
import cobo.chat.repository.custom.ChatRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRepository: JpaRepository<Chat, Int>, ChatRepositoryCustom {
    fun findByChatRoom(chatRoom: ChatRoom): List<Chat>
}