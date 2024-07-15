package cobo.auth.service.impl

import cobo.auth.config.jwt.JwtTokenProvider
import cobo.auth.config.response.CoBoResponse
import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.data.dto.auth.GetLoginRes
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.repository.UserRepository
import cobo.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository
) : AuthService {
    override fun getKakaoLogin(
        code: String,
        redirectUri: String
    ): ResponseEntity<CoBoResponseDto<GetLoginRes>> {

        val accessToken = jwtTokenProvider.getAccessToken(1)
        val refreshToken = jwtTokenProvider.getRefreshToken(1)

        val coBoResponse = CoBoResponse(GetLoginRes(accessToken, refreshToken, RegisterStateEnum.ACTIVE), CoBoResponseStatus.SUCCESS)

        return coBoResponse.getResponseEntityWithLog()
    }

}