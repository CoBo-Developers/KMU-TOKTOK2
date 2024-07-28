package cobo.chat.repository.impl

import cobo.chat.data.entity.Chat
import cobo.chat.repository.custom.ChatRepositoryCustom
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
): ChatRepositoryCustom {

    @Transactional
    override fun insert(chat: Chat) {
        jdbcTemplate.update(
            "INSERT INTO chat(comment, is_question, chat_room_id) " +
                    "VALUES (?, ?, ?)",
            chat.comment, chat.isQuestion, chat.chatRoom.id
        )
    }
}