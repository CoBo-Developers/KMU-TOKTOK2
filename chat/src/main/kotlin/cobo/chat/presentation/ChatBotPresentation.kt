package cobo.chat.presentation

import cobo.chat.service.ChatBotService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatBotPresentation(
    private val chatBotService: ChatBotService
) {


}