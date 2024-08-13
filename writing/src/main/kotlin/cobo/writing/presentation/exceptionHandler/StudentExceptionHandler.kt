package cobo.writing.presentation.exceptionHandler

import cobo.writing.config.response.CoBoResponse
import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.presentation.StudentPresentation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.NoSuchElementException

@RestControllerAdvice(basePackageClasses = [StudentPresentation::class])
class StudentExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun noSuchElementExceptionHandler(e: NoSuchElementException): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.CHATGPT_ERROR).getResponseEntity()
    }
}