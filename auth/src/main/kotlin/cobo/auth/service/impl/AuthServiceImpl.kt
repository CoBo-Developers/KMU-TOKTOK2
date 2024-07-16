package cobo.auth.service.impl

import cobo.auth.config.jwt.JwtTokenProvider
import cobo.auth.config.response.CoBoResponse
import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.data.dto.auth.GetLoginRes
import cobo.auth.data.entity.Oauth
import cobo.auth.data.entity.User
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import cobo.auth.repository.OauthRepository
import cobo.auth.repository.UserRepository
import cobo.auth.service.AuthService
import cobo.auth.service.oauth.impl.GoogleOauthServiceImpl
import cobo.auth.service.oauth.impl.KakaoOauthServiceImpl
import cobo.auth.service.oauth.impl.NaverOauthServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class AuthServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val oauthRepository: OauthRepository,
    private val kakaoOauthServiceImpl: KakaoOauthServiceImpl,
    private val googleOauthServiceImpl: GoogleOauthServiceImpl,
    private val naverOauthServiceImpl: NaverOauthServiceImpl
) : AuthService {
    override fun getKakaoLogin(
        code: String
    ): ResponseEntity<CoBoResponseDto<GetLoginRes>> {

        val oauthAccessToken = kakaoOauthServiceImpl.getAccessToken(
            code = code
        )

        val oauth = kakaoOauthServiceImpl.getOauth(oauthAccessToken)

        val user = if (oauth.user != null) {
            oauth.user
        } else{
            val user = userRepository.save(
                User(
                    id = null,
                    studentId = null,
                    role = RoleEnum.STUDENT,
                    registerState = RegisterStateEnum.INACTIVE
                )
            )
            CompletableFuture.runAsync {
                oauth.user = user
                oauthRepository.save(oauth)
            }
            user
        }

        val accessToken = jwtTokenProvider.getAccessToken(user?.id ?: throw NullPointerException())
        val refreshToken = jwtTokenProvider.getRefreshToken(user.id ?: throw NullPointerException())

        val coBoResponse = CoBoResponse(GetLoginRes(accessToken, refreshToken, user.registerState), CoBoResponseStatus.SUCCESS)

        return coBoResponse.getResponseEntityWithLog()
    }

    override fun getNaverLogin(code: String): ResponseEntity<CoBoResponseDto<GetLoginRes>> {
        val accessToken = jwtTokenProvider.getAccessToken(1)
        val refreshToken = jwtTokenProvider.getRefreshToken(1)

        val coBoResponse = CoBoResponse(GetLoginRes(accessToken, refreshToken, RegisterStateEnum.INACTIVE), CoBoResponseStatus.SUCCESS)

        return coBoResponse.getResponseEntityWithLog()
    }

}