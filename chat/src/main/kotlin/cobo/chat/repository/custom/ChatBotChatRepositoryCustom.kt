package cobo.chat.repository.custom

import cobo.chat.data.entity.ChatBotChat

interface ChatBotChatRepositoryCustom {

    fun findByStudentIdWithJDBC(studentId: String): List<ChatBotChat>
}