package cobo.auth.service.oauth.impl

import cobo.auth.data.entity.Oauth
import cobo.auth.data.enums.OauthTypeEnum
import cobo.auth.repository.OauthRepository
import cobo.auth.service.oauth.OauthService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class NaverOauthServiceImpl(
    @Value("\${naver.auth.client_id}")
    private val clientId: String,
    @Value("\${naver.auth.client_secret}")
    private val clientSecret: String,
    private val oauthRepository: OauthRepository
): OauthService, OauthServiceImpl(oauthRepository) {

    private final val naverAccessTokenServer = "https://nid.naver.com/oauth2.0/token"
    private final val naverUserInfoServer = "https://openapi.naver.com/v1/nid/me"

    override fun getOauth(code: String, isRemote: Boolean): Oauth {

        val accessToken = getAccessToken(code, isRemote)

        val restTemplate = RestTemplate()

        val naverUserInfo = restTemplate.exchange(
            RequestEntity<Any>(this.getHttpHeadersWithAuthorization(accessToken), HttpMethod.GET, URI.create(naverUserInfoServer)),
            NaverUserInfo::class.java
        ).body

        val naverUserId = naverUserInfo?.response?.id ?: ""

        return this.getOauthFromOauthIdAndOauthType(
            oauthId = naverUserId,
            oauthTypeEnum = OauthTypeEnum.NAVER,
            accessToken = accessToken)
    }

    override fun getAccessToken(code: String, isRemote: Boolean): String {
        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()

        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")

        val httpBody = LinkedMultiValueMap<String, String>()

        httpBody.add("client_id", clientId)
        httpBody.add("client_secret", clientSecret)
        httpBody.add("code", code)

        return restTemplate.postForObject(naverAccessTokenServer, this.getHttpEntity(httpBody), NaverAccessToken::class.java)?.accessToken ?: ""
    }

    private data class NaverAccessToken(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("state") val state: String?,
        @JsonProperty("error") val error: String?,
        @JsonProperty("error_description") val errorDescription: String?
    )

    private data class NaverUserInfo(
        @JsonProperty("resultcode") val resultCode: String,
        @JsonProperty("message") val message: String,
        @JsonProperty("response") val response: Response,
    )

    private data class Response(
        @JsonProperty("id") val id: String,
        @JsonProperty("nickname") val nickname: String?,
        @JsonProperty("name") val name: String?,
        @JsonProperty("email") val email: String?,
        @JsonProperty("gender") val gender: String?,
        @JsonProperty("age") val age: String?,
        @JsonProperty("birthday") val birthday: String?,
        @JsonProperty("profile_image") val profileImage: String?,
        @JsonProperty("birthyear") val birthyear: String?,
        @JsonProperty("mobile") val mobile: String?
    )
}