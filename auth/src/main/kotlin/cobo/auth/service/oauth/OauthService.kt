package cobo.auth.service.oauth

import cobo.auth.data.entity.Oauth

interface OauthService {

    fun getOauth(accessToken: String): Oauth
    fun getAccessToken(code: String, redirectUri: String): String
}