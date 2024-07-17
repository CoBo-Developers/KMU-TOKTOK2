package cobo.auth.auth

import cobo.auth.config.jwt.JwtTokenProvider
import cobo.auth.repository.OauthRepository
import cobo.auth.repository.UserRepository
import cobo.auth.service.AuthService
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AuthRegisterTest(
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val authService: AuthService,
    @Autowired private val oauthRepository: OauthRepository,
    @Autowired private val userRepository: UserRepository
) {

    companion object {
        @JvmStatic
        @BeforeAll
        internal fun beforeAll() {

        }

        @JvmStatic
        @AfterAll
        internal fun afterAll() {

        }
    }

    @BeforeEach
    fun beforeEach() {

    }

    @AfterEach
    fun afterEach() {

    }


}