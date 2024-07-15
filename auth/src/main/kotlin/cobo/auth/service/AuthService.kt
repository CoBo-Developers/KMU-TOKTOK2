package cobo.auth.service

import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.data.dto.auth.GetLoginRes
import org.springframework.http.ResponseEntity

interface AuthService {
    fun getKakaoLogin(code: String): ResponseEntity<CoBoResponseDto<GetLoginRes>>
}