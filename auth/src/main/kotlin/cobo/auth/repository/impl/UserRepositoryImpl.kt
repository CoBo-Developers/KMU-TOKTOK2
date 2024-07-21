package cobo.auth.repository.impl

import cobo.auth.data.entity.User
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import cobo.auth.repository.custom.UserRepositoryCustom
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.util.*

@Repository
class UserRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : UserRepositoryCustom {

    override fun findByStudentIdWithJDBC(studentId: String): Optional<User> {
        return try{
            Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT * FROM user WHERE student_id = ?", { rs, _ -> userRowMapper(rs)}, studentId)
            )
        }catch(e: EmptyResultDataAccessException){
            Optional.empty()
        }
    }

    @Transactional
    override fun updateStudentIdWithJDBC(id: Int, studentId: String, registerStateEnum: RegisterStateEnum) {
        jdbcTemplate.update("UPDATE user SET student_id = ?, register_state = ?  WHERE id = ?", studentId, registerStateEnum.value, id)
    }

    @Transactional
    override fun updateUserByStudentIdWithJDBC(studentId: String, role: Short, registerState: Short): Int {
        return jdbcTemplate.update("UPDATE user " +
                "SET role = ?, register_state = ? " +
                "WHERE student_id = ?", role, registerState, studentId)
    }

    private fun userRowMapper(resultSet: ResultSet): User {

        return User(
            id = resultSet.getInt("id"),
            studentId = resultSet.getString("student_id"),
            role = RoleEnum.from(resultSet.getShort("role")),
            registerState = RegisterStateEnum.from(resultSet.getShort("register_state")) ?: RegisterStateEnum.ACTIVE
        )

    }
}