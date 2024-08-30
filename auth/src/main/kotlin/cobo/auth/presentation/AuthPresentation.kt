package cobo.auth.presentation

import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.data.dto.auth.GetAuthLoginRes
import cobo.auth.data.dto.auth.PostAuthRegisterReq
import cobo.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
    fun getLocalKakaoLogin(
        @RequestParam code: String
    ): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>{
        return authService.getLocalKakaoLogin(code)
    }

    @GetMapping("/admin-kakao-login")
    @Operation(summary = "카카오 관리자 로그인 API")
    @Parameters(
        Parameter(name = "code", description = "카카오 로그인 code")
    )
    fun getAdminKakaoLogin(
        @RequestParam code: String
    ): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>{
        return authService.getAdminKakaoLogin(code)
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

    @GetMapping("/admin-naver-login")
    @Operation(summary = "네이버 관리자 로그인 API")
    @Parameters(
        Parameter(name = "code", description = "네이버 로그인 code")
    )
    fun getAdminNaverLogin(
        @RequestParam code: String
    ): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>{
        return authService.getAdminNaverLogin(code)
    }

    @PatchMapping("/login")
    @Operation(summary = "AccessToken 재발급 API")
    @ApiResponses(
        ApiResponse(responseCode = "400", description = "TOKEN IS EMPTY")
    )
    fun patchLogin(@Parameter(hidden = true, required = false) @RequestHeader(name = HttpHeaders.AUTHORIZATION) authorization: String?): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>>{
        return authService.patchLogin(authorization)
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입 API", description = "회원의 상태가 INACTIVE -> ACTIVE, 발급되는 토큰으로 다시 넣어줘야 합니다.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "404", description = "유효하지 않은 학생")
    )
    fun postRegister(@Valid @RequestBody postAuthRegisterReq: PostAuthRegisterReq, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<CoBoResponseDto<GetAuthLoginRes>> {
        return authService.postRegister(postAuthRegisterReq, authentication)
    }

}