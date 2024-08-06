package cobo.writing.repository.impl

import cobo.writing.repository.custom.AssignmentRepositoryCustom
import org.springframework.jdbc.core.JdbcTemplate

class AssignmentRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : AssignmentRepositoryCustom{

}