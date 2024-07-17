package cobo.auth.presentation

import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.data.dto.auth.GetLoginRes
import cobo.auth.data.dto.auth.PostRegisterReq
import cobo.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "인증 및 로그인 관련 API")
class AuthPresentation(
    private val authService: AuthService
) {

    @GetMapping("/kakao-login")
    @Operation(summary = "카카오 로그인 API")
    @Parameters(
        Parameter(name = "code", description = "카카오 로그인 code")
    )
    fun getKakaoLogin(
        @RequestParam code: String
    ): ResponseEntity<CoBoResponseDto<GetLoginRes>>{
        return authService.getKakaoLogin(code)
    }

    @GetMapping("/naver-login")
    @Operation(summary = "네이버 로그인 API")
    @Parameters(
        Parameter(name = "code", description = "네이버 로그인 code")
    )
    fun getNaverLogin(
        @RequestParam code: String
    ): ResponseEntity<CoBoResponseDto<GetLoginRes>>{
        return authService.getNaverLogin(code)
    }

    @GetMapping("/google-login")
    @Operation(summary = "구글 로그인 API")
    @Parameters(
        Parameter(name = "code", description = "구글 로그인 code")
    )
    fun getGoogleLogin(
        @RequestParam code: String
    ): ResponseEntity<CoBoResponseDto<GetLoginRes>>{
        return authService.getGoogleLogin(code)
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입 API", description = "회원의 상태가 INACTIVE -> ACTIVE")
    fun postRegister(@Valid @RequestBody postRegisterReq: PostRegisterReq, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return authService.postRegister(postRegisterReq, authentication)
    }

}