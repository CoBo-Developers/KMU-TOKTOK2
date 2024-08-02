package cobo.chat.data.dto.prof

import cobo.chat.data.enums.ChatStateEnum
import java.time.LocalDateTime

data class ProfGetElementRes(
    val comment: String,

    val localDateTime: LocalDateTime,

    val isQuestion: Boolean
)

data class ProfGetListRes(
    val totalElement: Long,

    val chatList: List<ProfGetListElementRes>
)

data class ProfGetListElementRes(

    val studentId: String,

    val comment: String,

    val chatState: ChatStateEnum,

    val localDateTime: LocalDateTime
)