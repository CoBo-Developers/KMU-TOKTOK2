package cobo.file.service

import cobo.file.config.response.CoBoResponseDto
import cobo.file.config.response.CoBoResponseStatus
import cobo.file.data.dto.category.CategoryGetListRes
import cobo.file.data.dto.professorCategory.ProfessorPostCategoryReq
import cobo.file.data.dto.professorCategory.ProfessorPutCategoryReq
import org.springframework.http.ResponseEntity

interface CategoryService {
    fun professorPost(professorPostCategoryReq: ProfessorPostCategoryReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
    fun professorPut(professorPutCategoryReq: ProfessorPutCategoryReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
    fun professorDelete(categoryId: Int): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
    fun getList(): ResponseEntity<CoBoResponseDto<CategoryGetListRes>>
}