package cobo.chat.repository

import cobo.chat.data.entity.ChatRoom
import cobo.chat.repository.custom.ChatRoomRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository: JpaRepository<ChatRoom, String>, ChatRoomRepositoryCustom {
}