package cobo.auth.repository

import cobo.auth.data.entity.Oauth
import org.springframework.data.jpa.repository.JpaRepository

interface OauthRepository: JpaRepository<Oauth, Int>{
}