package cobo.auth.data.dto.user

import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import io.swagger.v3.oas.annotations.media.Schema

data class PutUserReq(
    @Schema(description = "학생의 학번")
    val studentId: String,
    @Schema(description = "변경할 회원 상태")
    val registerStateEnum: RegisterStateEnum,
    @Schema(description = "변경할 권한 상태")
    val roleEnum: RoleEnum
)
