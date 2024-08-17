package cobo.file.presentation

import cobo.file.config.response.CoBoResponseDto
import cobo.file.data.dto.category.CategoryGetListRes
import cobo.file.service.CategoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/student/category")
@Tag(name = "카테고리 관련 API")
class CategoryPresentation(
    private val categoryService: CategoryService
) {

    @GetMapping("/list")
    @Operation(summary = "카테고리 조회 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "생성")
    )
    fun getList(): ResponseEntity<CoBoResponseDto<CategoryGetListRes>>{
        return categoryService.getList()
    }
}
