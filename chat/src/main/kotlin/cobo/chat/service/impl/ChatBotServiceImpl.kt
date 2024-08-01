package cobo.chat.service.impl

import cobo.chat.config.response.CoBoResponse
import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.config.response.CoBoResponseStatus
import cobo.chat.data.dto.chatBot.ChatBotGetElementRes
import cobo.chat.data.dto.chatBot.ChatBotPostReq
import cobo.chat.data.dto.chatBot.ChatBotPostRes
import cobo.chat.data.entity.ChatBotChat
import cobo.chat.repository.ChatBotChatRepository
import cobo.chat.service.ChatBotService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatBotServiceImpl(
    private val chatBotChatRepository: ChatBotChatRepository
): ChatBotService {
    override fun post(
        chatBotPostReq: ChatBotPostReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<ChatBotPostRes>> {

        val studentId = authentication.name

        val answer = getAnswerFromChatGPT(chatBotPostReq.question)

        chatBotChatRepository.save(
            ChatBotChat(
                id = null, studentId = studentId, answer = answer, question = chatBotPostReq.question, createdAt = null
        ))

        return CoBoResponse(
            ChatBotPostRes(answer),
            CoBoResponseStatus.SUCCESS
        ).getResponseEntity()
    }

    override fun get(authentication: Authentication): ResponseEntity<CoBoResponseDto<List<ChatBotGetElementRes>>> {
        return this.get(authentication.name)
    }

    override fun get(studentId: String): ResponseEntity<CoBoResponseDto<List<ChatBotGetElementRes>>> {
        return CoBoResponse(
            chatBotChatRepository.findByStudentIdWithJDBC(studentId).map{
                ChatBotGetElementRes(
                    question = it.question,
                    answer = it.answer,
                    createdAt = it.createdAt ?: LocalDateTime.now()
                )
            }, CoBoResponseStatus.SUCCESS
        ).getResponseEntity()
    }

    private fun getAnswerFromChatGPT(question: String): String{
        return question
    }

}