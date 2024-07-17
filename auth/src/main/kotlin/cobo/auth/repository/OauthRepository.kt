package cobo.auth.repository

import cobo.auth.data.entity.Oauth
import cobo.auth.data.enums.OauthTypeEnum
import cobo.auth.repository.custom.OauthRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OauthRepository: JpaRepository<Oauth, Int>, OauthRepositoryCustom{

    fun findByOauthIdAndOauthType(oauthId: String, oauthTypeEnum: OauthTypeEnum): Optional<Oauth>
}