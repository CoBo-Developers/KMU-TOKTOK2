package cobo.writing.config.response

import cobo.writing.config.LogFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

class CoBoResponse<T>(
    private val coBoResponseDto: CoBoResponseDto<T>
){

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LogFilter::class.java)
    }

    constructor(coBoResponseStatus: CoBoResponseStatus): this(CoBoResponseDto(coBoResponseStatus))
    constructor(data: T, coBoResponseStatus: CoBoResponseStatus): this(CoBoResponseDto(data, coBoResponseStatus))

    fun getResponseEntity(): ResponseEntity<CoBoResponseDto<T>> {
        return ResponseEntity(coBoResponseDto, HttpStatusCode.valueOf(coBoResponseDto.code / 10))
    }

    fun getResponseEntityWithLog(): ResponseEntity<CoBoResponseDto<T>> {
        val responseEntity = ResponseEntity(coBoResponseDto, HttpStatusCode.valueOf(coBoResponseDto.code / 10))
        logger.info("Response data: {}", responseEntity)
        return responseEntity
    }
}
