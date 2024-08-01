package cobo.chat.presentation.exceptionHandler

import cobo.chat.config.response.CoBoResponse
import cobo.chat.config.response.CoBoResponseStatus
import cobo.chat.data.exception.chatGPT.InvalidChatGPTResException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [ChatBotExceptionHandler::class])
class ChatBotExceptionHandler {

    @ExceptionHandler(InvalidChatGPTResException::class)
    fun invalidChatGPTResExceptionHandler(e: InvalidChatGPTResException) =
        CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.CANT_GET_RESOURCES).getResponseEntity()
}