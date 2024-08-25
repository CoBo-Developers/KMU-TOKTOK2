package cobo.file.professorCategory

import cobo.file.data.dto.professorCategory.ProfessorPostCategoryReq
import cobo.file.data.entity.Category
import cobo.file.repository.CategoryRepository
import cobo.file.service.CategoryService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.util.*

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProfessorCategoryPostTest @Autowired constructor(
    private val categoryRepository: CategoryRepository,
    private val categoryService: CategoryService
) {

    private val categoryList = mutableListOf<Category>()

    @AfterEach
    fun clear(){
        categoryRepository.deleteAll(categoryList)
        categoryList.clear()
    }

    @Test
    fun testPostCategory(){
        //given
        val previousCount = categoryRepository.count()
        val categoryName = UUID.randomUUID().toString()

        //when
        categoryService.professorPost(ProfessorPostCategoryReq(categoryName))

        //then
        val afterCount = categoryRepository.count()
        val category = categoryRepository.findTopByOrderByIdDesc().orElseThrow()

        assertEquals(previousCount + 1, afterCount)
        assertEquals(categoryName, category.name)
        assertEquals(false, category.deleted)
    }

    @Test
    fun testPostDuplicateCategory(){
        //given
        val categoryName = UUID.randomUUID().toString()
        val category = Category(id = null, name = categoryName)
        categoryRepository.save(category)
        categoryList.add(category)
        val previousCount = categoryRepository.count()

        //when
        val professorPostRes = categoryService.professorPost(ProfessorPostCategoryReq(categoryName))

        //then
        val afterCount = categoryRepository.count()

        assertEquals(HttpStatus.CONFLICT, professorPostRes.statusCode)
        assertEquals(previousCount, afterCount)
    }
}