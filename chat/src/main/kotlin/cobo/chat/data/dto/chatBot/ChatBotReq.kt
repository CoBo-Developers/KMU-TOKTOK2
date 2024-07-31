package cobo.chat.data.dto.chatBot

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class ChatBotPostReq(
    @JsonProperty("question")
    @Schema(description = "답변", example = "자장면입니다.")
    val question: String
)