package cobo.writing.repository.impl

import cobo.writing.repository.custom.WritingRepositoryCustom
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class WritingRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
): WritingRepositoryCustom {
}