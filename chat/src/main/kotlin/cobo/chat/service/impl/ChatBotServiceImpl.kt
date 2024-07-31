package cobo.chat.service.impl

import cobo.chat.config.response.CoBoResponse
import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.config.response.CoBoResponseStatus
import cobo.chat.data.dto.chatBot.ChatBotPostReq
import cobo.chat.data.dto.chatBot.ChatBotPostRes
import cobo.chat.repository.ChatBotChatRepository
import cobo.chat.service.ChatBotService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class ChatBotServiceImpl(
    private val chatBotChatRepository: ChatBotChatRepository
): ChatBotService {
    override fun post(
        chatBotPostReq: ChatBotPostReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<ChatBotPostRes>> {
        return CoBoResponse(
            ChatBotPostRes(getAnswerFromChatGPT(chatBotPostReq.question)),
            CoBoResponseStatus.SUCCESS
        ).getResponseEntity()
    }

    private fun getAnswerFromChatGPT(question: String): String{
        return "IM DTH"
    }

}