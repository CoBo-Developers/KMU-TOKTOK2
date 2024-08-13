package cobo.writing.repository.impl

import cobo.writing.data.entity.Assignment
import cobo.writing.data.entity.Writing
import cobo.writing.data.enums.WritingStateEnum
import cobo.writing.repository.custom.WritingRepositoryCustom
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.util.*

@Repository
class WritingRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
): WritingRepositoryCustom {

    @Transactional
    override fun ifExistsUpdateElseInsert(writing: Writing): Int {
        return jdbcTemplate.update(
            "INSERT INTO writing (student_id, assignment_id, content, state, created_at, updated_at, submitted_at)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "content = VALUES(content), " +
                    "state = VALUES(state), " +
                    "updated_at = VALUES(updated_at)," +
                    "submitted_at = VALUES(submitted_at) ",
            writing.studentId, writing.assignment.id, writing.content, writing.state.value, writing.createdAt, writing.updatedAt, writing.submittedAt
        )
    }

    override fun updateStateByAssignmentIdAndStudentIdWithJDBC(
        writingState: Short,
        assignmentId: Int,
        studentId: String
    ): Int {
        return jdbcTemplate.update(
            "UPDATE writing SET writing.state = ? " +
                    "WHERE assignment_id = ? AND student_id = ?",
            writingState, assignmentId, studentId)
    }

    override fun findByAssignmentAndStudentIdWithJDBC(assignment: Assignment, studentId: String): Optional<Writing> {
        return try {
            Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT writing.id, writing.student_id, writing.assignment_id, writing.content, writing.state, writing.created_at, writing.updated_at, writing.submitted_at" +
                        " FROM writing " +
                        "WHERE writing.assignment_id = ? AND writing.student_id = ?",
                { rs, _ -> writingRowMapper(rs) },
                assignment.id,
                studentId))
        }catch (emptyResultDataAccessException: EmptyResultDataAccessException){
            Optional.empty()
        }

    }

    override fun findByAssignmentIdOrderByStatePagingWithJDBC(assignmentId: Int, page: Int, pageSize: Int): List<Writing> {
        val sql = "SELECT writing.id, writing.student_id, writing.assignment_id, writing.content, writing.state, writing.created_at, writing.updated_at, writing.submitted_at " +
                "FROM writing WHERE writing.assignment_id = ? ORDER BY writing.state, writing.id LIMIT ?, ?"
        return jdbcTemplate.query(
            sql, {rs, _ -> writingRowMapper(rs)}, assignmentId, page, pageSize
        )
    }

    override fun countByAssignmentIdWithJDBC(assignmentId: Int): Long {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM writing WHERE assignment_id = ?", Long::class.java, assignmentId)

    }

    private fun writingRowMapper(resultSet: ResultSet): Writing {
        return Writing(
            id = resultSet.getInt("id"),
            studentId = resultSet.getString("student_id"),
            assignment = Assignment(resultSet.getInt("assignment_id")),
            content = resultSet.getString("content"),
            state = WritingStateEnum.from(resultSet.getShort("state"))!!,
            createdAt = resultSet.getTimestamp("created_at").toLocalDateTime(),
            updatedAt = resultSet.getTimestamp("updated_at").toLocalDateTime(),
            submittedAt = resultSet.getTimestamp("submitted_at").toLocalDateTime()
        )
    }


}