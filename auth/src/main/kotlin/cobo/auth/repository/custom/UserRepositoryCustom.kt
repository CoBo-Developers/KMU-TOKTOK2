package cobo.auth.repository.custom

import cobo.auth.data.entity.User
import java.util.*

interface UserRepositoryCustom {

    fun findByStudentIdWithJDBC(studentId: String) : Optional<User>
    fun updateStudentIdWithJDBC(id: Int, studentId: String)
}