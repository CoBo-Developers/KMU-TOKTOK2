package cobo.auth.service.oauth.impl

import cobo.auth.service.oauth.OauthService
import org.springframework.stereotype.Service

@Service
class KakaoOauthServiceImpl : OauthService {
    override fun getId(accessToken: String): Long {
        TODO("Not yet implemented")
    }

    override fun getAccessToken(code: String, redirectUri: String): String {
        TODO("Not yet implemented")
    }
}