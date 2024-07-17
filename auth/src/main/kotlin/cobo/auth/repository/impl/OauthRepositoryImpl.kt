package cobo.auth.repository.impl

import cobo.auth.repository.custom.OauthRepositoryCustom
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class OauthRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
): OauthRepositoryCustom{

    @Transactional
    override fun updateUserIdByUserIdWithJDBC(oldUserId: Int, newUserId: Int) {
        jdbcTemplate.update(
            "UPDATE oauth " +
                    "INNER JOIN user " +
                    "ON oauth.user_id = user.id " +
                    "SET oauth.user_id = ? " +
                    "WHERE user.id = ?", newUserId, oldUserId
        )
    }
}