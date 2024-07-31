package cobo.chat.data.dto.chatBot

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ChatBotPostRes(
    @Schema(description = "질문", example = "오늘의 메뉴는?")
    val answer: String
)

data class ChatBotGetElementRes(
    @Schema(description = "작성 일자", example = "2024-07-31T12:23:03.396369")
    val createdAt: LocalDateTime,
    @Schema(description = "답변", example = "자장면입니다.")
    val answer: String,
    @Schema(description = "질문", example = "오늘의 메뉴는?")
    val question: String,
)