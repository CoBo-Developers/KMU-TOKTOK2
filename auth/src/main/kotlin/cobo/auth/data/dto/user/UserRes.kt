package cobo.auth.data.dto.user

import cobo.auth.data.entity.User

data class UserRes(
    val studentId: String?,
    val role: String?,
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
    val totalElements: Long
){
    constructor(
        users: List<User>,
        totalElements: Long
    ) : this(
        users = users.map{UserRes(it)},
        totalElements = totalElements
    )
}