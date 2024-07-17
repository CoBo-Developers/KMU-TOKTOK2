package cobo.auth.auth

import cobo.auth.config.jwt.JwtTokenProvider
import cobo.auth.data.entity.Oauth
import cobo.auth.data.entity.User
import cobo.auth.data.enums.OauthTypeEnum
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
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

    private val kakaoUser = User(
        id = -1,
        studentId = null,
        role = RoleEnum.STUDENT,
        registerState = RegisterStateEnum.INACTIVE
    )

    private val naverUser = User(
        id = -2,
        studentId = null,
        role = RoleEnum.STUDENT,
        registerState = RegisterStateEnum.INACTIVE
    )

    private val googleUser = User(
        id = -3,
        studentId = null,
        role = RoleEnum.STUDENT,
        registerState = RegisterStateEnum.INACTIVE
    )

    private val kakaoOauth = Oauth(
        id = -1,
        user = kakaoUser,
        oauthId = "testKakaoUser",
        oauthType = OauthTypeEnum.KAKAO,
        accessToken = null
    )

    private val naverOauth = Oauth(
        id = -2,
        user = naverUser,
        oauthId = "testNaverUser",
        oauthType = OauthTypeEnum.NAVER,
        accessToken = null
    )

    private val googleOauth = Oauth(
        id = -3,
        user = googleUser,
        oauthId = "testGoogleUser",
        oauthType = OauthTypeEnum.GOOGLE,
        accessToken = null
    )



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