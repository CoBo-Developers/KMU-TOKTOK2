package cobo.chat.repository

import cobo.chat.data.entity.ChatRoom
import cobo.chat.data.enums.ChatStateEnum
import cobo.chat.repository.custom.ChatRoomRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository: JpaRepository<ChatRoom, String>, ChatRoomRepositoryCustom {

    fun findAllByOrderByChatStateEnum(): List<ChatRoom>
    fun countByChatStateEnum(chatStateEnum: ChatStateEnum): Long
}