package cobo.auth.config.response

import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

class CoBoResponse<T>(
    private val coBoResponseDto: CoBoResponseDto<T>
){

    companion object {
        private val logger = LogManager.getLogger(CoBoResponse::class.java)
    }

    constructor(coBoResponseStatus: CoBoResponseStatus): this(CoBoResponseDto(coBoResponseStatus))
    constructor(data: T, coBoResponseStatus: CoBoResponseStatus): this(CoBoResponseDto(data, coBoResponseStatus))

    fun getResponseEntity():ResponseEntity<CoBoResponseDto<T>>{
        return ResponseEntity(coBoResponseDto, HttpStatusCode.valueOf(coBoResponseDto.code / 10))
    }

    fun getResponseEntityWithLog(): ResponseEntity<CoBoResponseDto<T>> {
        val responseEntity = ResponseEntity(coBoResponseDto, HttpStatusCode.valueOf(coBoResponseDto.code / 10))
        logger.info("Response data: {}", responseEntity)
        return responseEntity
    }
}
