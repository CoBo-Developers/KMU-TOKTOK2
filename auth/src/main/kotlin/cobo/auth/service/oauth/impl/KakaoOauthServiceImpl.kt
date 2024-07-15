package cobo.auth.service.oauth.impl

import cobo.auth.data.entity.Oauth
import cobo.auth.repository.OauthRepository
import cobo.auth.service.oauth.OauthService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class KakaoOauthServiceImpl(
    @Value("\${kakao.auth.client_id}")
    private val clientId: String,
    @Value("\${kakao.auth.redirect_uri}")
    private val redirectUri: String,
    private val oauthRepository: OauthRepository
) : OauthService {

    private final val kakaoAccessTokenServer = "https://kauth.kakao.com/oauth/token"
    private final val kakaoUserInfoServer =  "https://kapi.kakao.com/v2/user/me"

    override fun getOauth(accessToken: String): Oauth {

        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
        httpHeaders.add("Authorization", "Bearer $accessToken")

        val kakaoUserInfo =  restTemplate.exchange(
            RequestEntity<Any>(httpHeaders, HttpMethod.POST, URI.create(kakaoUserInfoServer)),
            KakaoUserInfo::class.java
        ).body

        val kakaoUserId = kakaoUserInfo?.id ?: 0L

        return oauthRepository.findByOauthId(kakaoUserId).orElseThrow()
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

        return restTemplate.postForObject(kakaoAccessTokenServer, HttpEntity(httpBody, httpHeaders), KakaoAccessToken::class.java)?.accessToken ?: ""
    }

    private data class KakaoAccessToken(
        @JsonProperty("access_token")
        val accessToken: String
    )

    private data class KakaoUserInfo(
        @JsonProperty("id") val id: Long,
        @JsonProperty("connected_at") val connectedAt: String,
        @JsonProperty("properties") val properties: Properties,
        @JsonProperty("kakao_account") val kakaoAccount: KakaoAccount
    )

    private data class Properties(
        @JsonProperty("nickname") val nickname: String
    )

    private data class KakaoAccount(
        @JsonProperty("profile_nickname_needs_agreement") val profileNicknameNeedsAgreement: Boolean,
        @JsonProperty("profile") val profile: Profile
    )

    private data class Profile(
        @JsonProperty("nickname") val nickname: String,
        @JsonProperty("is_default_nickname") val isDefaultNickname: Boolean,
        @JsonProperty("profile_image_url") val profileImageUrl:String
    )
}