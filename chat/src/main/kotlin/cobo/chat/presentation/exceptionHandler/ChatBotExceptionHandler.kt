package cobo.chat.presentation.exceptionHandler

import cobo.chat.config.response.CoBoResponse
import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.config.response.CoBoResponseStatus
import cobo.chat.presentation.ProfPresentation
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [ProfPresentation::class])
class ChatBotExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun dataIntegrityViolationExceptionHandler(e: DataIntegrityViolationException): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.CANT_SAVE).getResponseEntity()
    }
}