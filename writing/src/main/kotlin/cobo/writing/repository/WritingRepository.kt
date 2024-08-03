package cobo.writing.repository

import cobo.writing.data.entity.Writing
import org.springframework.data.jpa.repository.JpaRepository

interface WritingRepository: JpaRepository<Writing, Int> {
}