package cobo.auth.data.entity

import cobo.auth.data.enums.OauthTypeEnum
import jakarta.persistence.*

@Entity
@Table(
    indexes = [
        Index(name = "idx_type_id", columnList = "oauthType, oauthId"),
        Index(name = "idx_token", columnList = "accessToken")
    ]
)
data class Oauth(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @ManyToOne
    var user: User,
    var oauthId: Int,
    @Enumerated(EnumType.ORDINAL)
    var oauthType: OauthTypeEnum,
    var accessToken: String?
)
