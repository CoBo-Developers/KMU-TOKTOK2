package cobo.writing.repository.impl

import cobo.writing.data.entity.Writing
import cobo.writing.repository.custom.WritingRepositoryCustom
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

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
}