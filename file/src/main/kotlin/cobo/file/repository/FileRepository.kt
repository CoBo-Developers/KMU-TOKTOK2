package cobo.file.repository

import cobo.file.data.entity.Category
import cobo.file.data.entity.File
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository: JpaRepository<File, Int> {
    fun findByCategory(category: Category): List<File>
}