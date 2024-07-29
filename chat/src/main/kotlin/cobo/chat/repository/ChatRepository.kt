package cobo.chat.repository

import cobo.chat.data.entity.Chat
import cobo.chat.data.entity.ChatRoom
import cobo.chat.repository.custom.ChatRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface ChatRepository: JpaRepository<Chat, Int>, ChatRepositoryCustom {
    fun findByChatRoom(chatRoom: ChatRoom): List<Chat>
    @Transactional
    fun deleteByChatRoom(chatRoom: ChatRoom)
    fun countByChatRoom(chatRoom: ChatRoom): Long
}