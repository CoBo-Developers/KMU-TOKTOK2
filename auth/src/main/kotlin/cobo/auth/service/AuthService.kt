package cobo.auth.service

import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.data.dto.auth.GetAuthLoginRes
import cobo.auth.data.dto.auth.PostAuthRegisterReq
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

interface AuthService {
    fun getKakaoLogin(code: String): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>
    fun getNaverLogin(code: String): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>
    fun getGoogleLogin(code: String): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>
    fun postRegister(
        postAuthRegisterReq: PostAuthRegisterReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>
    fun getKakaoLocalLogin(code: String): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>
    fun getGoogleLocalLogin(code: String): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>
    fun patchLogin(authorization: String?): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>
}