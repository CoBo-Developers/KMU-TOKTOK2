package cobo.auth.repository

import cobo.auth.data.entity.User
import cobo.auth.repository.custom.UserRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, Int>, UserRepositoryCustom {

    fun findByStudentId(studentId: String): Optional<User>
}