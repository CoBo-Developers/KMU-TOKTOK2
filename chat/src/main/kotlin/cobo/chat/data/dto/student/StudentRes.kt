package cobo.chat.data.dto.student

import cobo.chat.data.enum.ChatStateEnum
import java.time.LocalDateTime

data class StudentGetRes(

    val comment: String,

    val localDateTime: LocalDateTime,

    val chatStateEnum: ChatStateEnum
    )

data class StudentGetElementRes(
    val comment: String,

    val isQuestion: Boolean,

    val localDateTime: LocalDateTime
)