package cobo.file.professorCategory.parent

import cobo.file.data.entity.Category
import cobo.file.repository.CategoryRepository
import org.junit.jupiter.api.AfterEach

open class CategoryClear(
    private val categoryRepository: CategoryRepository
) {

    open val categoryList = mutableListOf<Category>()

    @AfterEach
    fun clear(){
        categoryRepository.deleteAll(categoryList)
        categoryList.clear()
    }
}