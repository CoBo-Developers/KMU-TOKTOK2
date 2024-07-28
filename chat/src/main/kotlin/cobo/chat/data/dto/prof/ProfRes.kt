package cobo.chat.data.dto.prof

import cobo.chat.data.enum.ChatStateEnum
import java.time.LocalDateTime

data class ProfGetRes(
    val comment: String,

    val localDateTime: LocalDateTime,

    val chatStateEnum: ChatStateEnum
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