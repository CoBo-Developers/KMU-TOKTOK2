package cobo.auth.data.entity

import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import jakarta.persistence.*

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @Column(unique = true)
    var studentId: String?,
    @Enumerated(EnumType.ORDINAL)
    var role: RoleEnum,
    @Enumerated(EnumType.ORDINAL)
    var registerState: RegisterStateEnum
){
    constructor(id: Int):this(
        id = id, studentId = null, role = RoleEnum.STUDENT, registerState = RegisterStateEnum.ACTIVE
    )
}