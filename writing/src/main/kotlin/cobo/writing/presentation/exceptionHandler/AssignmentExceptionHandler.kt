package cobo.writing.presentation.exceptionHandler

import cobo.writing.config.response.CoBoResponse
import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.presentation.AssignmentPresentation
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [AssignmentPresentation::class])
class AssignmentExceptionHandler {

    @ExceptionHandler(EntityNotFoundException::class)
    fun entityNotFoundExceptionHandler(e: EntityNotFoundException): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.NOT_FOUNT_DATA).getResponseEntity()
    }
}