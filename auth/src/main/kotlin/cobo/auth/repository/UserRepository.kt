package cobo.auth.repository

import cobo.auth.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int> {
}