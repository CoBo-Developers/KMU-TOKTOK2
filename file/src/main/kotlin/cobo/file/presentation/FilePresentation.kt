package cobo.file.presentation

import cobo.file.config.response.CoBoResponseDto
import cobo.file.data.dto.file.FileGetListRes
import cobo.file.service.FileService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/file")
@Tag(name = "파일 관련 API")
class FilePresentation(
    private val fileService: FileService) {

    @GetMapping("/list")
    @Operation(summary = "파일리스트 조회 API")
    @Parameters(
        Parameter(name = "categoryId", description = "검색할 카테고리 아이디")
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "400", description = "잘못된 파라미터 전달"),
        ApiResponse(responseCode = "403", description = "인증 실패")
    )
    fun getList(@RequestParam categoryId: Int?): ResponseEntity<CoBoResponseDto<FileGetListRes>> {
        return fileService.getList(categoryId)
    }

    @GetMapping
    @Operation(summary = "파일 다운로드 API", description = "다운로드 할 파일의 FileId 입력")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "403", description = "인증 실패"),
        ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음")
    )
    fun get(@RequestParam("fileId") fileId: Int): ResponseEntity<Resource>{
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}