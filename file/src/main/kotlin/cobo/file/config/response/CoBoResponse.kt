package cobo.file.config.response

import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

class CoBoResponse<T>(
    private val coBoResponseDto: CoBoResponseDto<T>
){

    constructor(coBoResponseStatus: CoBoResponseStatus): this(CoBoResponseDto(coBoResponseStatus))
    constructor(data: T, coBoResponseStatus: CoBoResponseStatus): this(CoBoResponseDto(data, coBoResponseStatus))

    fun getResponseEntity(): ResponseEntity<CoBoResponseDto<T>> {
        return ResponseEntity(coBoResponseDto, HttpStatusCode.valueOf(coBoResponseDto.code / 10))
    }
}
