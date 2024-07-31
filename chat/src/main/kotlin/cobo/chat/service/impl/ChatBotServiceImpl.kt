package cobo.chat.service.impl

import cobo.chat.repository.ChatBotChatRepository
import cobo.chat.service.ChatBotService
import org.springframework.stereotype.Service

@Service
class ChatBotServiceImpl(
    private val chatBotChatRepository: ChatBotChatRepository
): ChatBotService {


}