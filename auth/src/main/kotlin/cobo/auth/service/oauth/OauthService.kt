package cobo.auth.service.oauth

import cobo.auth.data.entity.Oauth

interface OauthService {

    fun getOauth(code: String): Oauth
    fun getAccessToken(code: String): String
}