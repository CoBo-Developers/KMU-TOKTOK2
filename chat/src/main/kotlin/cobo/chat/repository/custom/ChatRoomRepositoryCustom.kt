package cobo.chat.repository.custom

import cobo.chat.data.entity.ChatRoom

interface ChatRoomRepositoryCustom {
    fun ifExistUpdateElseInsert(chatRoom: ChatRoom)
}