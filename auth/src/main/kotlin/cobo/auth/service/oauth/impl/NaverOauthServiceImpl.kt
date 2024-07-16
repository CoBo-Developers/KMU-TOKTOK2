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
class NaverOauthServiceImpl(
    @Value("\${naver.auth.client_id}")
    private val clientId: String,
    @Value("\${naver.auth.redirect_uri}")
    private val redirectUri: String,
    @Value("\${naver.auth.client_secret}")
    private val clientSecret: String,
    private val oauthRepository: OauthRepository
){

    private final val naverAccessTokenServer = "https://nid.naver.com/oauth2.0/token"
    private final val naverUserInfoServer = ""

    fun getOauth(accessToken: String): Oauth {
      TODO()
    }

    fun getAccessToken(code: String, state: String): String {
        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()

        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")

        val httpBody = LinkedMultiValueMap<String, String>()

        httpBody.add("grant_type", "authorization_code")
        httpBody.add("client_id", clientId)
        httpBody.add("client_secret", clientSecret)
        httpBody.add("code", code)
        httpBody.add("state", "http%3A%2F%2Flocalhost%3A8080%2Fapi%2Fauth%2Fnaver-login")

        println(httpBody)

        val result = restTemplate.postForObject(naverAccessTokenServer, HttpEntity(httpBody, httpHeaders), NaverAccessToken::class.java)

        println(result)

        return ""
    }

    private data class NaverAccessToken(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("state") val state: String,
        @JsonProperty("error") val error: String?,
        @JsonProperty("error_description") val errorDescription: String?
    )
}