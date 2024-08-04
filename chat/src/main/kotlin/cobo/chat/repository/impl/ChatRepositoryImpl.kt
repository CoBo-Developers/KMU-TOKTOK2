package cobo.chat.repository.impl

import cobo.chat.data.entity.Chat
import cobo.chat.data.entity.ChatRoom
import cobo.chat.repository.custom.ChatRepositoryCustom
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.util.concurrent.CompletableFuture

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

    override fun findByChatRoomWithJDBC(chatRoom: ChatRoom): List<Chat> {
        return jdbcTemplate.query(
            "SELECT * FROM chat WHERE chat.chat_room_id = ?", {rs, _ -> chatRowMapper(rs)},chatRoom.id
        )
    }

    override fun findByChatRoomAndUpdateWithJDBC(chatRoom: ChatRoom): List<Chat> {
        val chatRoomUpdateCompletableFuture = CompletableFuture.runAsync {
            jdbcTemplate.update(
                "UPDATE chat_room SET chat_state_enum = ? WHERE chat_room.id = ?",
                chatRoom.chatStateEnum.value, chatRoom.id
            )
        }

        val chatRoomListCompletableFuture = CompletableFuture.supplyAsync {
            jdbcTemplate.query(
                "SELECT * FROM chat WHERE chat.chat_room_id = ?",
                { rs, _ -> chatRowMapper(rs) }, chatRoom.id
            )
        }

        return CompletableFuture.allOf(chatRoomUpdateCompletableFuture, chatRoomListCompletableFuture)
            .thenApply {
                chatRoomListCompletableFuture.get()
            }.get()
    }

    private fun chatRowMapper(resultSet: ResultSet): Chat{
        return Chat(
            id = resultSet.getInt("id"),
            chatRoom = ChatRoom(resultSet.getString("chat_room_id")),
            isQuestion = resultSet.getBoolean("is_question"),
            createdAt = resultSet.getTimestamp("created_at").toLocalDateTime(),
            comment = resultSet.getString("comment")
        )
    }
}