package cobo.chat.data.dto.chatBot

import java.time.LocalDateTime

data class ChatBotPostRes(
    val answer: String
)

data class ChatBotGetElementRes(
    val createdAt: LocalDateTime,
    val answer: String,
    val question: String,
)