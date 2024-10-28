package cobo.chat.data.dto.student

import java.time.LocalDateTime

data class StudentGetElementRes(
    val comment: String,

    val isQuestion: Boolean,

    val localDateTime: LocalDateTime
)