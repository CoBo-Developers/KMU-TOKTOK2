package cobo.file.professorCategory

import cobo.file.data.dto.professorCategory.ProfessorPutCategoryReq
import cobo.file.data.entity.Category
import cobo.file.professorCategory.parent.CategoryClear
import cobo.file.repository.CategoryRepository
import cobo.file.service.CategoryService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.util.*

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProfessorCategoryPutTest @Autowired constructor(
    private val categoryRepository: CategoryRepository,
    private val categoryService: CategoryService
): CategoryClear(categoryRepository) {

    @Test
    fun testPutCategory(){
        //given
        val category = Category(id = null, name = UUID.randomUUID().toString())
        categoryRepository.save(category)
        categoryList.add(category)
        val previousCount = categoryRepository.count()
        val name = UUID.randomUUID().toString()

        //when
        val professorPutCategoryRes = categoryService.professorPut(
            ProfessorPutCategoryReq(categoryId = category.id!!, name = name)
        )

        //then
        assertEquals(HttpStatus.OK, professorPutCategoryRes.statusCode)

        assertEquals(previousCount, categoryRepository.count())
        val expectedCategory = Category(id = category.id, name = name)

        assertEquals(expectedCategory, categoryRepository.findById(category.id!!).orElseThrow())
    }
}