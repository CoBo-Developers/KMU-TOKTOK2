package cobo.chat.presentation

import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.data.dto.chatBot.ChatBotGetElementRes
import cobo.chat.data.dto.chatBot.ChatBotPostReq
import cobo.chat.data.dto.chatBot.ChatBotPostRes
import cobo.chat.service.ChatBotService
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatBotPresentation(
    private val chatBotService: ChatBotService
) {

    @PostMapping
    fun post(@RequestBody chatBotPostReq: ChatBotPostReq, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<CoBoResponseDto<ChatBotPostRes>> {
        return chatBotService.post(chatBotPostReq, authentication)
    }

    @GetMapping
    fun get(@Parameter(hidden = true) authentication: Authentication): ResponseEntity<CoBoResponseDto<List<ChatBotGetElementRes>>>{
        return chatBotService.get(authentication)
    }

    @GetMapping("/student")
    fun get(@RequestParam studentId: String): ResponseEntity<CoBoResponseDto<List<ChatBotGetElementRes>>>{
        return chatBotService.get(studentId)
    }
}