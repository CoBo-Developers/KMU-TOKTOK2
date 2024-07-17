package cobo.auth.service

import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.data.dto.auth.GetLoginRes
import cobo.auth.data.dto.auth.PostRegisterReq
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

interface AuthService {
    fun getKakaoLogin(code: String): ResponseEntity<CoBoResponseDto<GetLoginRes>>
    fun getNaverLogin(code: String): ResponseEntity<CoBoResponseDto<GetLoginRes>>
    fun getGoogleLogin(code: String): ResponseEntity<CoBoResponseDto<GetLoginRes>>
    fun postRegister(
        postRegisterReq: PostRegisterReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
}