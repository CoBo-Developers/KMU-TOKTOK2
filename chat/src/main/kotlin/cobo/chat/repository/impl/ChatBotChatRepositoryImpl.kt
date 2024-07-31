package cobo.chat.repository.impl

import cobo.chat.repository.ChatBotChatRepository
import cobo.chat.repository.custom.ChatBotChatRepositoryCustom
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ChatBotChatRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : ChatBotChatRepositoryCustom {
}