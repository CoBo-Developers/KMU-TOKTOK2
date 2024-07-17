package cobo.auth.repository.custom

interface OauthRepositoryCustom {

    fun updateUserIdByUserIdWithJDBC(oldUserId: Int, newUserId: Int)
}