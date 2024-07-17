package cobo.auth.repository.impl

import cobo.auth.data.entity.User
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import cobo.auth.repository.custom.UserRepositoryCustom
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : UserRepositoryCustom {


}