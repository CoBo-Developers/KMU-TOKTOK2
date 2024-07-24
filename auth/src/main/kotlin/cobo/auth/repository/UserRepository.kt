package cobo.auth.repository

import cobo.auth.data.entity.User
import cobo.auth.repository.custom.UserRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface UserRepository: JpaRepository<User, Int>, UserRepositoryCustom {

    fun findByStudentIdNotNullOrderByStudentIdAsc(pageable: Pageable): Page<User>

    fun findByStudentId(studentId: String): Optional<User>
}