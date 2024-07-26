package cobo.auth.repository.custom

import cobo.auth.data.dto.user.PutUserReq
import cobo.auth.data.entity.User
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import java.util.*

interface UserRepositoryCustom {

    fun findByStudentIdWithJDBC(studentId: String) : Optional<User>
    fun updateStudentIdWithJDBC(id: Int, studentId: String, registerStateEnum: RegisterStateEnum)
    fun updateUserByStudentIdWithJDBC(studentId: String, role: Short, registerState: Short, newStudentId: String): Int
}