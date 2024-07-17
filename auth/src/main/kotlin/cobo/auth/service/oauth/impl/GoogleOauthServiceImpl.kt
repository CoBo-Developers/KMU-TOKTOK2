package cobo.auth.service.oauth.impl

import cobo.auth.data.entity.Oauth
import cobo.auth.data.enums.OauthTypeEnum
import cobo.auth.repository.OauthRepository
import cobo.auth.service.oauth.OauthService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class GoogleOauthServiceImpl(
    @Value("\${google.auth.client_id}")
    private val clientId: String,
    @Value("\${google.auth.redirect_uri}")
    private val redirectUri: String,
    @Value("\${google.auth.client_secret}")
    private val clientSecret: String,
    private val oauthRepository: OauthRepository
): OauthService, OauthServiceImpl(oauthRepository) {

    private final val googleAccessTokenServer = "https://oauth2.googleapis.com/token"
    private final val googleUserInfoServer = "https://www.googleapis.com/oauth2/v2/userinfo"

    override fun getOauth(code: String): Oauth {

        val accessToken = getAccessToken(code)

        val restTemplate = RestTemplate()

        val googleUserInfo = restTemplate.exchange(
            RequestEntity<Any>(this.getHttpHeadersWithAuthorization(accessToken), HttpMethod.GET, URI.create(googleUserInfoServer)),
            GoogleUserInfo::class.java
        ).body

        val googleUserId = googleUserInfo?.id ?: ""

        return this.getOauthFromOauthIdAndOauthType(
            oauthId = googleUserId,
            oauthTypeEnum = OauthTypeEnum.GOOGLE,
            accessToken = accessToken)
    }

    override fun getAccessToken(code: String): String {
        val restTemplate = RestTemplate()

        val httpBody = LinkedMultiValueMap<String, String>()

        httpBody.add("client_id", clientId)
        httpBody.add("code",code)
        httpBody.add("client_secret", clientSecret)
        httpBody.add("redirect_uri", redirectUri)

        val result =  restTemplate.postForObject(googleAccessTokenServer, this.getHttpEntity(httpBody), GoogleAccessToken::class.java)?.accessToken ?: ""

        return result
    }

    private data class GoogleAccessToken(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("expires_in") val expiresIn: Int,
        @JsonProperty("token_type") val tokenType: String,
        @JsonProperty("scope") val scope: String,
        @JsonProperty("id_token") val idToken: String,
    )

    private data class GoogleUserInfo(
        @JsonProperty("id") val id: String?,
        @JsonProperty("email") val email: String?,
        @JsonProperty("verified_email") val verifiedEmail: Boolean?,
        @JsonProperty("name") val name: String?,
        @JsonProperty("given_name") val givenName: String?,
        @JsonProperty("family_name") val familyName: String?,
        @JsonProperty("picture") val picture: String?,
        @JsonProperty("locale") val locale: String?
    )
}