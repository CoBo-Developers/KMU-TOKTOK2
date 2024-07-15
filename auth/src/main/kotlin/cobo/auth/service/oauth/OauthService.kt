package cobo.auth.service.oauth

interface OauthService {

    fun getId(accessToken: String): Long
    fun getAccessToken(code: String, redirectUri: String): String
}