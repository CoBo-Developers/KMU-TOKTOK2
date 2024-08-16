package cobo.file.service.impl

import cobo.file.config.response.CoBoResponse
import cobo.file.config.response.CoBoResponseDto
import cobo.file.config.response.CoBoResponseStatus
import cobo.file.data.dto.professorCategory.ProfessorPostCategoryReq
import cobo.file.data.entity.Category
import cobo.file.repository.CategoryRepository
import cobo.file.service.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
): CategoryService {

    override fun professorPost(professorPostCategoryReq: ProfessorPostCategoryReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        val category = Category(
            name = professorPostCategoryReq.category,
            deleted = false
        )

        categoryRepository.save(category)

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.CREATED).getResponseEntity()
    }
}