package cobo.file.repository

import cobo.file.data.entity.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<Category, String> {
}