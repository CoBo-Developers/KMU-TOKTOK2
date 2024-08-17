package cobo.file.presentation

import cobo.file.data.dto.professorFile.ProfessorFilePostReq
import cobo.file.service.FileService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/professor/category")
@Tag(name = "파일 관련 API(교수, 개발자만 사용 가능)")
class ProfessorFilePresentation(
    private val fileService: FileService
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "파일 업로드 API", description = "교수, 개발자 권한만 사용 가능")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "업로드 성공", content = arrayOf(Content()))
    )
    private fun post(@ModelAttribute professorFilePostReq: ProfessorFilePostReq): ResponseEntity<HttpStatus> {
        return fileService.professorPost(professorFilePostReq)
    }
}