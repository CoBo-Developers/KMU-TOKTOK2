package cobo.auth.service.oauth

import cobo.auth.data.entity.Oauth

interface OauthService {

    fun getOauth(code: String, isRemote: Boolean): Oauth
    fun getAccessToken(code: String, isRemote: Boolean): String
}