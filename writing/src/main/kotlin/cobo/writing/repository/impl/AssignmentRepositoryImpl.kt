package cobo.writing.repository.impl

import cobo.writing.data.dto.student.StudentGetListResElement
import cobo.writing.data.enums.WritingStateEnum
import cobo.writing.repository.custom.AssignmentRepositoryCustom
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class AssignmentRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : AssignmentRepositoryCustom{

    override fun findByUserWithJDBC(studentId: String?): List<StudentGetListResElement> {

        val sql = "SELECT assignment.id, assignment.title, assignment.description, assignment.score, assignment.start_date, assignment.end_date, " +
                "writing.state FROM assignment " +
                "LEFT JOIN writing ON assignment.id = writing.assignment_id " +
                "AND writing.student_id = ?"
        return jdbcTemplate.query(
            sql, {rs, _ -> studentGetListResElementRowMapper(rs)}, studentId
        )
    }

    private fun studentGetListResElementRowMapper(resultSet: ResultSet): StudentGetListResElement{
        return StudentGetListResElement(
            id = resultSet.getInt("id"),
            title = resultSet.getString("title"),
            description = resultSet.getString("description"),
            score = resultSet.getInt("score"),
            startDate = resultSet.getTimestamp("start_date").toLocalDateTime().toLocalDate(),
            endDate = resultSet.getTimestamp("end_date").toLocalDateTime().toLocalDate(),
            writingState = resultSet.getShort("state")
        )
    }
}