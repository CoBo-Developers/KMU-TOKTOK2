package cobo.file.config.response

import java.time.LocalDateTime

data class CoBoResponseDto<T>(
    val code: Int,
    val message: String,
    val time: LocalDateTime,
    val data: T?
){
    constructor(coBoResponseStatus: CoBoResponseStatus): this(
        code = coBoResponseStatus.code,
        message = coBoResponseStatus.message,
        time = LocalDateTime.now(),
        data = null
    )

    constructor(data: T, coBoResponseStatus: CoBoResponseStatus): this(
        code = coBoResponseStatus.code,
        message = coBoResponseStatus.message,
        time = LocalDateTime.now(),
        data = data
    )
}