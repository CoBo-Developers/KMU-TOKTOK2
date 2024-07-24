package cobo.auth.service.oauth.impl

import cobo.auth.data.entity.Oauth
import cobo.auth.data.enums.OauthTypeEnum
import cobo.auth.repository.OauthRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.util.LinkedMultiValueMap
import java.util.concurrent.CompletableFuture

open class OauthServiceImpl(
    private val oauthRepository: OauthRepository
) {

    fun getHttpEntity(httpBody: LinkedMultiValueMap<String, String>): HttpEntity<LinkedMultiValueMap<String, String>> {
        val httpHeaders = HttpHeaders()
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")

        httpBody.add("grant_type", "authorization_code")

        return HttpEntity(httpBody, httpHeaders)
    }

    fun getHttpHeadersWithAuthorization(accessToken: String): HttpHeaders{
        val httpHeaders = HttpHeaders()
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
        httpHeaders.add("Authorization", "Bearer $accessToken")

        return httpHeaders
    }

    fun getOauthFromOauthIdAndOauthType(oauthId: String, oauthTypeEnum: OauthTypeEnum): Oauth {
        val optionalOauth = oauthRepository.findByOauthIdAndOauthType(oauthId, oauthTypeEnum)

        if (optionalOauth.isPresent) {
            CompletableFuture.supplyAsync{
                optionalOauth.get()
            }.thenApply {
                oauthRepository.save(it)
            }
            return optionalOauth.get()
        }
        else{
            return oauthRepository.save(Oauth(
                id = null,
                user = null,
                oauthId = oauthId,
                oauthType = oauthTypeEnum
            ))
        }
    }
}