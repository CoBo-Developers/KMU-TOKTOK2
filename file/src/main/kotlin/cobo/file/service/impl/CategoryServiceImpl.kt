package cobo.file.service.impl

import cobo.file.config.response.CoBoResponse
import cobo.file.config.response.CoBoResponseDto
import cobo.file.config.response.CoBoResponseStatus
import cobo.file.data.dto.category.CategoryGetListRes
import cobo.file.data.dto.category.CategoryGetListResElement
import cobo.file.data.dto.professorCategory.ProfessorPostCategoryReq
import cobo.file.data.dto.professorCategory.ProfessorPutCategoryReq
import cobo.file.data.entity.Category
import cobo.file.repository.CategoryRepository
import cobo.file.service.CategoryService
import org.springframework.dao.DataIntegrityViolationException
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

        try{
            categoryRepository.save(category)
        }catch(dataIntegrityViolationException: DataIntegrityViolationException){
            return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.EXIST_DATA).getResponseEntity()
        }

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.CREATED).getResponseEntity()
    }

    override fun professorDelete(categoryId: Int): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        categoryRepository.deleteById(categoryId)
        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun getList(): ResponseEntity<CoBoResponseDto<CategoryGetListRes>> {
        val categories = categoryRepository.findAll().map{
            CategoryGetListResElement(
                id = it.id!!,
                name = it.name
            )
        }

        return CoBoResponse(CategoryGetListRes(categories), CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun professorPut(professorPutCategoryReq: ProfessorPutCategoryReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        try{
            val category = categoryRepository.findById(professorPutCategoryReq.categoryId).orElseThrow{NullPointerException()}

            category.name = professorPutCategoryReq.name

            categoryRepository.save(category)

            return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
        }catch(nullPointerException: NullPointerException){
            return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.NOT_FOUND_CATEGORY).getResponseEntity()
        }
    }
}