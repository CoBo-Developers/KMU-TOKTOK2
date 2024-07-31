package cobo.chat.repository

import cobo.chat.data.entity.ChatBotChat
import cobo.chat.repository.custom.ChatBotChatRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface ChatBotChatRepository: JpaRepository<ChatBotChat, Int>, ChatBotChatRepositoryCustom {
    fun findByStudentId(studentId: String): List<ChatBotChat>

    @Transactional
    fun deleteByStudentId(studentId: String)
}