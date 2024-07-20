package cobo.auth.data.dto.user

import cobo.auth.data.entity.User
import io.swagger.v3.oas.annotations.media.Schema

data class UserRes(
    @Schema(description = "학번")
    val studentId: String?,
    @Schema(description = "권한")
    val role: String?,
    @Schema(description = "학번 입력 완료 여부")
    val registerState: String?
){
    constructor(user: User) : this(
        studentId = user.studentId,
        role = user.role.name,
        registerState = user.registerState.name
    )
}

data class GetUserListRes(
    val users: List<UserRes>,
    @Schema(description = "전체 유저의 수")
    val totalElements: Long
)