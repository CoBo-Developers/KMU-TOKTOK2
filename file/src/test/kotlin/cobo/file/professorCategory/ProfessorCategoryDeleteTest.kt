package cobo.file.professorCategory

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
import java.util.UUID

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProfessorCategoryDeleteTest @Autowired constructor(
    private val categoryRepository: CategoryRepository,
    private val categoryService: CategoryService
): CategoryClear(categoryRepository){

    @Test
    fun testDeleteCategory(){
        //given
        val categoryName = UUID.randomUUID().toString()
        val category = Category(id = null, name = categoryName)
        categoryRepository.save(category)
        categoryList.add(category)

        val previousCount = categoryRepository.count()

        //when
        val professorDeleteRes = categoryService.professorDelete(categoryId = category.id!!)

        //then
        val afterCount = categoryRepository.count()

        assertEquals(HttpStatus.OK, professorDeleteRes.statusCode)
        assertEquals(previousCount - 1, afterCount)

    }


}