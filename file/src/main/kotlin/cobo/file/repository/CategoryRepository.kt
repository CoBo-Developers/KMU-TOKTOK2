package cobo.file.repository

import cobo.file.data.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface CategoryRepository: JpaRepository<Category, Int> {
    fun findByName(oldCategory: String): Optional<Category>
    @Transactional
    fun deleteByName(category: String)
}