package cobo.auth.service.oauth.impl

import cobo.auth.data.entity.Oauth
import cobo.auth.repository.OauthRepository
import cobo.auth.service.oauth.OauthService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class KakaoOauthServiceImpl(
    @Value("\${kakao.auth.client_id}")
    private val clientId: String,
    private val oauthRepository: OauthRepository
) : OauthService {

    private val kakaoAccessTokenServer = "https://kauth.kakao.com/oauth/token"
    override fun getOauth(accessToken: String): Oauth {
        TODO("Not yet implemented")
    }

    override fun getAccessToken(code: String, redirectUri: String): String {

        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()

        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")

        val httpBody = LinkedMultiValueMap<String, String>()

        httpBody.add("grant_type","authorization_code")
        httpBody.add("client_id", clientId)
        httpBody.add("redirect_uri", redirectUri)
        httpBody.add("code",code)

        return restTemplate.postForObject(kakaoAccessTokenServer, HttpEntity(httpBody, httpHeaders), KakaoAccessToken::class.java)?.accessToken ?: ""
    }

    private class KakaoAccessToken(
        @JsonProperty("access_token")
        val accessToken: String,
    )
}