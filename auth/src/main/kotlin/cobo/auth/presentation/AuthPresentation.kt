package cobo.auth.presentation

import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.data.dto.auth.GetAuthLoginRes
import cobo.auth.data.dto.auth.PostAuthRegisterReq
import cobo.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
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
    ): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>{
        return authService.getKakaoLogin(code)
    }

    @GetMapping("/kakao-local-login")
    @Operation(summary = "카카오 로컬 로그인 API")
    @Parameters(
        Parameter(name = "code", description = "카카오 로그인 code")
    )
    fun getKakaoLocalLogin(
        @RequestParam code: String
    ): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>{
        return authService.getKakaoLocalLogin(code)
    }

    @GetMapping("/naver-login")
    @Operation(summary = "네이버 로그인 API")
    @Parameters(
        Parameter(name = "code", description = "네이버 로그인 code")
    )
    fun getNaverLogin(
        @RequestParam code: String
    ): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>{
        return authService.getNaverLogin(code)
    }

    @GetMapping("/google-login")
    @Operation(summary = "구글 로그인 API")
    @Parameters(
        Parameter(name = "code", description = "구글 로그인 code")
    )
    fun getGoogleLogin(
        @RequestParam code: String
    ): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>{
        return authService.getGoogleLogin(code)
    }

    @GetMapping("/google-local-login")
    @Operation(summary = "구글 로그인 API")
    @Parameters(
        Parameter(name = "code", description = "구글 로그인 code")
    )
    fun getGoogleLocalLogin(
        @RequestParam code: String
    ): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>{
        return authService.getGoogleLocalLogin(code)
    }

    @PatchMapping("/login")
    @Operation(summary = "AccessToken 재발급 API")
    fun patchLogin(@RequestHeader(name = HttpHeaders.AUTHORIZATION) authorization: String): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>{
        return authService.patchLogin(authorization)
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입 API", description = "회원의 상태가 INACTIVE -> ACTIVE, 발급되는 토큰으로 다시 넣어줘야 합니다.")
    fun postRegister(@Valid @RequestBody postAuthRegisterReq: PostAuthRegisterReq, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>> {
        return authService.postRegister(postAuthRegisterReq, authentication)
    }

}