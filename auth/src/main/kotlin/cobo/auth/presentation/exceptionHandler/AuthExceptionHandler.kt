package cobo.auth.presentation.exceptionHandler

import cobo.auth.config.response.CoBoResponse
import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.presentation.AuthPresentation
import jakarta.security.auth.message.AuthException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [AuthPresentation::class])
class AuthExceptionHandler {

    @ExceptionHandler(IllegalAccessException::class)
    fun illegalAccessExceptionHandler(): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntityWithLog()
    }

    @ExceptionHandler(NullPointerException::class)
    fun nullPointerExceptionHandler(): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.NOT_FOUND_USER).getResponseEntityWithLog()
    }

    @ExceptionHandler(IndexOutOfBoundsException::class)
    fun indexOutOfBoundsExceptionHandler(): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.NOT_AUTHORIZATION).getResponseEntityWithLog()
    }
}