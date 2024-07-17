package cobo.auth.repository

import cobo.auth.data.entity.User
import cobo.auth.repository.custom.UserRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int>, UserRepositoryCustom {
}