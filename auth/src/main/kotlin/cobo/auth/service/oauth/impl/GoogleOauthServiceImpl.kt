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
class GoogleOauthServiceImpl(
    @Value("\${google.auth.client_id}")
    private val clientId: String,
    @Value("\${google.auth.redirect_uri}")
    private val redirectUri: String,
    @Value("\${google.auth.client_secret}")
    private val clientSecret: String,
    private val oauthRepository: OauthRepository
): OauthService {

    private final val googleAccessTokenServer = "https://oauth2.googleapis.com/token"
    private final val googleUserInfoServer = ""

    override fun getOauth(code: String): Oauth {

        val accessToken = getAccessToken(code)

        TODO("Not yet implemented")
    }

    override fun getAccessToken(code: String): String {
        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()

        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")

        val httpBody = LinkedMultiValueMap<String, String>()

        httpBody.add("grant_type","authorization_code")
        httpBody.add("client_id", clientId)
        httpBody.add("redirect_uri", redirectUri)
        httpBody.add("code",code)
        httpBody.add("client_secret", clientSecret)

        val result =  restTemplate.postForObject(googleAccessTokenServer, HttpEntity(httpBody, httpHeaders), GoogleAccessToken::class.java) ?: ""

        println(result)

        return result.toString()
    }

    private data class GoogleAccessToken(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("expires_in") val expiresIn: Int,
        @JsonProperty("token_type") val tokenType: String,
        @JsonProperty("scope") val scope: String,
        @JsonProperty("id_token") val idToken: String,
    )
}