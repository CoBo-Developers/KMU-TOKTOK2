package cobo.auth.repository

import cobo.auth.data.entity.Oauth
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OauthRepository: JpaRepository<Oauth, Int>{

    fun findByOauthId(oauthId: Long): Optional<Oauth>
}