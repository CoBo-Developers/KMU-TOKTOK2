package cobo.file.service

import cobo.file.config.response.CoBoResponseDto
import cobo.file.config.response.CoBoResponseStatus
import cobo.file.data.dto.professorCategory.ProfessorPostCategoryReq
import org.springframework.http.ResponseEntity

interface CategoryService {
    fun professorPost(professorPostCategoryReq: ProfessorPostCategoryReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
}