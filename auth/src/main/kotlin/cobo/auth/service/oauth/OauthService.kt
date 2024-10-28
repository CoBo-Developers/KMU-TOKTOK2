package cobo.auth.service.oauth

import cobo.auth.data.entity.Oauth

interface OauthService {

    fun getOauth(code: String, redirectUri: String): Oauth
    fun getAccessToken(code: String, redirectUri: String): String
}