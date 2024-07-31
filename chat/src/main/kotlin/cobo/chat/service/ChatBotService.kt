package cobo.chat.service

import cobo.chat.config.response.CoBoResponse
import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.data.dto.chatBot.ChatBotPostReq
import cobo.chat.data.dto.chatBot.ChatBotPostRes
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

interface ChatBotService {
    fun post(
        chatBotPostReq: ChatBotPostReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<ChatBotPostRes>>

}
