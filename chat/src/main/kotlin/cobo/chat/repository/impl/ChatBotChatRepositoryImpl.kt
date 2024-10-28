package cobo.chat.repository.impl

import cobo.chat.data.entity.ChatBotChat
import cobo.chat.repository.ChatBotChatRepository
import cobo.chat.repository.custom.ChatBotChatRepositoryCustom
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class ChatBotChatRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : ChatBotChatRepositoryCustom {

    override fun findByStudentIdWithJDBC(studentId: String): List<ChatBotChat> {
        return jdbcTemplate.query(
            "SELECT * FROM chat_bot_chat where student_id = ? ORDER BY id",
            {rs, _ -> chatBotChatRowMapper(rs)},
            studentId
        )
    }

    private fun chatBotChatRowMapper(resultSet: ResultSet): ChatBotChat {
        return ChatBotChat(
            id = resultSet.getInt("id"),
            studentId = resultSet.getString("student_id"),
            question = resultSet.getString("question"),
            answer = resultSet.getString("answer"),
            createdAt = resultSet.getTimestamp("created_at").toLocalDateTime(),
        )
    }
}