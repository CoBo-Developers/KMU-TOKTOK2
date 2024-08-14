package cobo.writing.repository.impl

import cobo.writing.data.dto.student.StudentGetListResElement
import cobo.writing.data.entity.Assignment
import cobo.writing.repository.AssignmentRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Timestamp
import java.time.LocalDate
import java.util.*
import kotlin.collections.LinkedHashMap

@Repository
class AssignmentRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : AssignmentRepository {

    private val assignmentHashMap: LinkedHashMap<Int, Assignment> = LinkedHashMap(this.findAllByDatabase().associateBy { it.id!! })

    override fun findTopByOrderByIdDesc(): Optional<Assignment> {
        return Optional.ofNullable(assignmentHashMap.values.last())
    }

    override fun findById(id: Int): Optional<Assignment> {
        return Optional.ofNullable(assignmentHashMap[id])
    }

    @Transactional
    override fun save(assignment: Assignment): Assignment {
        val savedAssignment = this.saveInDB(assignment)
        if(!assignment.deleted) {
            this.assignmentHashMap[savedAssignment.id!!] = savedAssignment
        }
        return savedAssignment
    }

    override fun findAll(): List<Assignment> {
        return assignmentHashMap
            .entries
            .sortedBy { it.key }
            .associate{it.toPair()}
            .values
            .toList()
    }

    @Transactional
    override fun delete(assignment: Assignment) {

        assignmentHashMap.remove(assignment.id)

        jdbcTemplate.update(
            "UPDATE assignment set deleted = true WHERE id = ?", assignment.id
        )
    }

    @Transactional
    override fun deleteAll(assignmentList: MutableList<Assignment>) {
        for (assignment in assignmentList) {
            assignmentHashMap.remove(assignment.id)

            jdbcTemplate.update(
                "UPDATE assignment set deleted = true WHERE id = ?", assignment.id
            )
        }
    }

    override fun count(): Long {
        return assignmentHashMap.count().toLong()
    }

    override fun deleteAll() {
        val iterator = assignmentHashMap.values.iterator()

        while (iterator.hasNext()) {
            val assignment = iterator.next()

            jdbcTemplate.update(
                "UPDATE assignment set deleted = true WHERE id = ?", assignment.id
            )

            iterator.remove()
        }
    }

    override fun saveAll(assignmentList: List<Assignment>): List<Assignment> {
        return assignmentList.map{
            val savedAssignment = this.saveInDB(it)
            if(!it.deleted)
                this.assignmentHashMap[savedAssignment.id!!] = savedAssignment
            savedAssignment
        }
    }

    override fun findByUserWithJDBC(studentId: String?): List<StudentGetListResElement> {

        val sql = "SELECT assignment.id, assignment.title, assignment.description, assignment.score, assignment.start_date, assignment.end_date, writing.state, writing.score AS writing_score " +
                "FROM assignment " +
                "LEFT JOIN writing ON assignment.id = writing.assignment_id " +
                "AND writing.student_id = ? " +
                "WHERE assignment.deleted = false"
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
            writingState = resultSet.getShort("state"),
            writingScore = resultSet.getInt("writing_score"),
        )
    }

    private fun findAllByDatabase(): List<Assignment>{
        return jdbcTemplate.query(
            "SELECT * FROM assignment WHERE deleted = false"
        ){
            rs, _ -> Assignment(
                id = rs.getInt("id"),
                title = rs.getString("title"),
                description = rs.getString("description"),
                prompt = rs.getString("prompt"),
                score = rs.getInt("score"),
                startDate = rs.getTimestamp("start_date").toLocalDateTime().toLocalDate(),
                endDate = rs.getTimestamp("end_date").toLocalDateTime().toLocalDate(),
                deleted = rs.getBoolean("deleted"),
            )
        }
    }

    private fun saveInDB(assignment: Assignment): Assignment{
        val sql = "INSERT INTO assignment (deleted, description, end_date, prompt, score, start_date, title) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)"

        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({
                connection ->
            val preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setBoolean(1, assignment.deleted)
            preparedStatement.setString(2, assignment.description)
            preparedStatement.setTimestamp(3, Timestamp.valueOf((assignment.endDate ?: LocalDate.now()).atStartOfDay()))
            preparedStatement.setString(4, assignment.prompt)
            preparedStatement.setInt(5, assignment.score ?: 0)
            preparedStatement.setTimestamp(6, Timestamp.valueOf((assignment.startDate ?: LocalDate.now()).atStartOfDay()))
            preparedStatement.setString(7, assignment.title)
            preparedStatement
        }, keyHolder)


        assignment.id = keyHolder.key!!.toInt()

        return assignment
    }
}