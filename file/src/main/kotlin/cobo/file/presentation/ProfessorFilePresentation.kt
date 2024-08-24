package cobo.file.presentation

import cobo.file.config.response.CoBoResponseDto
import cobo.file.config.response.CoBoResponseStatus
import cobo.file.data.dto.professorFile.ProfessorFilePatchReq
import cobo.file.data.dto.professorFile.ProfessorFilePostReq
import cobo.file.service.FileService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/professor/file")
@Tag(name = "파일 관련 API(교수, 개발자만 사용 가능)")
class ProfessorFilePresentation(
    private val fileService: FileService
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "파일 업로드 API", description = "교수, 개발자 권한만 사용 가능")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "업로드 성공", content = arrayOf(Content()))
    )
    private fun post(@ModelAttribute professorFilePostReq: ProfessorFilePostReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return fileService.professorPost(professorFilePostReq)
    }

    @DeleteMapping
    @Operation(summary = "파일 삭제 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content())),
        ApiResponse(responseCode = "403", description = "인증 실패", content = arrayOf(Content()))
    )
    fun delete(@RequestParam fileId: List<Int>): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>{
        return fileService.professorDelete(fileId)
    }

    @PatchMapping
    @Operation(summary = "파일 수정 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공")
    )
    fun patch(@RequestBody professorFilePatchReq: ProfessorFilePatchReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>{
        return fileService.professorPatch(professorFilePatchReq)
    }

}