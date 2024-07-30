package cobo.chat.repository.impl

import cobo.chat.data.dto.prof.ProfGetListElementRes
import cobo.chat.data.entity.ChatRoom
import cobo.chat.data.enum.ChatStateEnum
import cobo.chat.repository.custom.ChatRoomRepositoryCustom
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ChatRoomRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
): ChatRoomRepositoryCustom {

    @Transactional
    override fun ifExistUpdateElseInsert(chatRoom: ChatRoom) {
        jdbcTemplate.update(
            "INSERT INTO chat_room (id, chat_state_enum) " +
                    "VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE chat_state_enum = VALUES(chat_state_enum)",
            chatRoom.id, chatRoom.chatStateEnum.value)
    }

    override fun update(chatRoom: ChatRoom): Int {
        return jdbcTemplate.update(
            "UPDATE chat_room SET chat_state_enum = ? WHERE id = ?", chatRoom.chatStateEnum.value, chatRoom.id
        )
    }

    override fun findByPagingWithJDBC(page: Int, pageSize: Int): List<ProfGetListElementRes> {

        return jdbcTemplate.query(
            "SELECT chat.chat_room_id, chat.comment, chat_state_enum, chat.created_at FROM chat " +
                    "LEFT JOIN chat_room ON chat_room.id = chat.chat_room_id " +
                    "WHERE (chat.id) in " +
                    "(SELECT MAX(chat.id) FROM chat GROUP BY chat.chat_room_id) " +
                    "ORDER BY chat_room.chat_state_enum, chat.created_at desc LIMIT ?, ?",
            { resultSet, _ ->
                ProfGetListElementRes(
                    studentId = resultSet.getString("chat_room_id"),
                    comment = resultSet.getString("comment"),
                    chatState = ChatStateEnum.from(resultSet.getShort("chat_state_enum"))!!,
                    localDateTime = resultSet.getTimestamp("created_at").toLocalDateTime(),
                )
            }, page, pageSize)
    }
}