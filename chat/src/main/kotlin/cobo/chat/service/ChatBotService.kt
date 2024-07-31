package cobo.chat.service

import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.data.dto.chatBot.ChatBotGetElementRes
import cobo.chat.data.dto.chatBot.ChatBotPostReq
import cobo.chat.data.dto.chatBot.ChatBotPostRes
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

interface ChatBotService {
    fun post(
        chatBotPostReq: ChatBotPostReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<ChatBotPostRes>>

    fun get(authentication: Authentication): ResponseEntity<CoBoResponseDto<List<ChatBotGetElementRes>>>
    fun get(studentId: String): ResponseEntity<CoBoResponseDto<List<ChatBotGetElementRes>>>

}
