package cobo.chat.repository

import cobo.chat.data.entity.ChatBotChat
import cobo.chat.repository.custom.ChatBotChatRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface ChatBotChatRepository: JpaRepository<ChatBotChat, Int>, ChatBotChatRepositoryCustom {
    fun findByStudentId(studentId: String): List<ChatBotChat>
}