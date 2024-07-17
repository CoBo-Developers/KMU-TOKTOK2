package cobo.auth.repository.impl

import cobo.auth.repository.custom.OauthRepositoryCustom
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class OauthRepositoryCustom(
    private val jdbcTemplate: JdbcTemplate
): OauthRepositoryCustom{


}