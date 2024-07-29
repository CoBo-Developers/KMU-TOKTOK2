package cobo.chat.repository.impl

import cobo.chat.data.entity.ChatRoom
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
}