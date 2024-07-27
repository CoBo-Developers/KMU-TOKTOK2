package cobo.auth.presentation.exceptionHandler

import cobo.auth.config.response.CoBoResponse
import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.presentation.UserPresentation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [UserPresentation::class])
class UserExceptionHandler {

    @ExceptionHandler(NullPointerException::class)
    fun nullPointerExceptionHandler(nullPointerException: KotlinNullPointerException): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.NOT_FOUND_USER).getResponseEntityWithLog()
    }
}