package cobo.file.presentation

import cobo.file.config.response.CoBoResponseDto
import cobo.file.config.response.CoBoResponseStatus
import cobo.file.data.dto.professorCategory.ProfessorPostCategoryReq
import cobo.file.data.dto.professorCategory.ProfessorPutCategoryReq
import cobo.file.service.CategoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
        return categoryService.professorPost(professorPostCategoryReq)
    }

    @PutMapping
    @Operation(summary = "카테고리 수정 API", description = "해당 카테고리명을 수정")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "수정 성공")
    )
    fun put(@RequestBody professorPutCategoryReq: ProfessorPutCategoryReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return categoryService.professorPut(professorPutCategoryReq)
    }

    @DeleteMapping
    @Operation(summary = "카테고리 삭제 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "삭제 성공")
    )
    fun delete(@RequestParam categoryId: Int): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>{
        return categoryService.professorDelete(categoryId)
    }
}