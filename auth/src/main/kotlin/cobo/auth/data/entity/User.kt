package cobo.auth.data.entity

import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @Column(unique = true)
    var studentId: String?,
    var role: RoleEnum,
    var registerState: RegisterStateEnum
)
