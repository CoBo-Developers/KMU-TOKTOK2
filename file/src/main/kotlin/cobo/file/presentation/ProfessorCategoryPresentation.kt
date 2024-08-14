package cobo.file.presentation

import cobo.file.config.response.CoBoResponseDto
import cobo.file.config.response.CoBoResponseStatus
import cobo.file.data.dto.professorCategory.ProfessorPostCategoryReq
import cobo.file.service.CategoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/professor/category")
@Tag(name = "카테고리 관련 API(교수, 개발자만 사용 가능)")
class ProfessorCategoryPresentation(
    private val categoryService: CategoryService
) {

    @PostMapping
    @Operation(summary = "카테고리 추가 API", description = "해당 카테고리를 추가")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "생성")
    )
    fun post(@RequestBody professorPostCategoryReq: ProfessorPostCategoryReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}