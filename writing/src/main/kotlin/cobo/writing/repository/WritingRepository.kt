package cobo.writing.repository

import cobo.writing.data.entity.Writing
import cobo.writing.repository.custom.WritingRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface WritingRepository: JpaRepository<Writing, Int>, WritingRepositoryCustom {
}