package cobo.chat.repository

import cobo.chat.data.entity.Chat
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRepository: JpaRepository<Chat, Int> {
}