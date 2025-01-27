package cobo.auth.presentation

import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.data.dto.user.GetUserListRes
import cobo.auth.data.dto.user.PutUserReq
import cobo.auth.data.dto.user.GetUserRes
import cobo.auth.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
@Tag(name = "사용자 조회 및 수정 API")
class UserPresentation(
    private val userService: UserService
) {
    @GetMapping("/list")
    @Operation(summary = "유저 목록 조회")
    @Parameters(
        Parameter(name = "page", description = "조회할 페이지의 번호"),
        Parameter(name = "pageSize", description = "한 페이지에 조회할 데이터의 수")
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "조회 성공", content = [Content(schema = Schema(implementation = GetUserListRes::class))])
    )
    fun getList(
        @RequestParam page: Int,
        @RequestParam pageSize: Int): ResponseEntity<CoBoResponseDto<GetUserListRes>> {
        return userService.getList(page, pageSize)
    }

    @PutMapping
    @Operation(summary = "유저 정보 수정")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "수정 성공")
    )
    fun patch(@RequestBody putUserReq: PutUserReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return userService.put(putUserReq)
    }

    @GetMapping("/search")
    @Operation(summary = "유저 정보 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "조회 성공"),
        ApiResponse(responseCode = "404", description = "해당하는 유저가 존재하지 않음",
            content = [Content(schema = Schema(implementation = CoBoResponseDto::class))])
    )
    fun getSearch(@RequestParam studentId: String, @RequestParam pageSize: Int, @RequestParam page: Int): ResponseEntity<CoBoResponseDto<GetUserListRes>>{
        return userService.getSearch(studentId, pageSize, page)
    }
}