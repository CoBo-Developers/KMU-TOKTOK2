package cobo.chat.repository.custom

import cobo.chat.data.dto.prof.ProfGetListElementRes
import cobo.chat.data.entity.ChatRoom

interface ChatRoomRepositoryCustom {
    fun ifExistUpdateElseInsert(chatRoom: ChatRoom)
    fun update(chatRoom: ChatRoom): Int
    fun findByPagingWithJDBC(page: Int, pageSize: Int): List<ProfGetListElementRes>
}