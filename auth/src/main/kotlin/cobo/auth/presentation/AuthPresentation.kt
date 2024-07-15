package cobo.auth.presentation

import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.data.dto.auth.GetLoginRes
import cobo.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
@Tag(name = "인증 및 로그인 관련 API")
class AuthPresentation(
    private val authService: AuthService
) {

    @GetMapping("/kakao-login")
    @Operation(summary = "카카오 로그인 API")
    @Parameters(
        Parameter(name = "code", description = "카카오 로그인 code"),
        Parameter(name = "redirectUri", description = "카카오 로그인 redirect_uri")
    )
    fun getKakaoLogin(
        @RequestParam code: String
    ): ResponseEntity<CoBoResponseDto<GetLoginRes>>{
        return authService.getKakaoLogin(code)
    }
}