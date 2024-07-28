package cobo.chat.repository.custom

import cobo.chat.data.entity.Chat

interface ChatRepositoryCustom {
    fun insert(chat: Chat)
}