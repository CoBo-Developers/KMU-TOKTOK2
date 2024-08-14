package cobo.file.service.impl

import cobo.file.config.response.CoBoResponseDto
import cobo.file.config.response.CoBoResponseStatus
import cobo.file.data.dto.professorCategory.ProfessorPostCategoryReq
import cobo.file.service.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(

): CategoryService {

    override fun professorPost(professorPostCategoryReq: ProfessorPostCategoryReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        TODO("Not yet implemented")
    }
}