package cobo.auth.auth

import cobo.auth.config.jwt.JwtTokenProvider
import cobo.auth.data.dto.auth.PostRegisterReq
import cobo.auth.data.entity.Oauth
import cobo.auth.data.entity.User
import cobo.auth.data.enums.OauthTypeEnum
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import cobo.auth.repository.OauthRepository
import cobo.auth.repository.UserRepository
import cobo.auth.service.AuthService
import org.junit.jupiter.api.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

@SpringBootTest
class AuthRegisterTest(
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val authService: AuthService,
    @Autowired private val oauthRepository: OauthRepository,
    @Autowired private val userRepository: UserRepository
) {

    companion object {

        private val logger: Logger = LoggerFactory.getLogger(AuthRegisterTest::class.java)

        private val kakaoUser = User(
            id = null,
            studentId = null,
            role = RoleEnum.STUDENT,
            registerState = RegisterStateEnum.INACTIVE
        )

        private val naverUser = User(
            id = null,
            studentId = null,
            role = RoleEnum.STUDENT,
            registerState = RegisterStateEnum.INACTIVE
        )

        private val googleUser = User(
            id = null,
            studentId = null,
            role = RoleEnum.STUDENT,
            registerState = RegisterStateEnum.INACTIVE
        )

        private val kakaoOauth = Oauth(
            id = null,
            user = kakaoUser,
            oauthId = "testKakaoUser",
            oauthType = OauthTypeEnum.KAKAO,
            accessToken = null
        )

        private val naverOauth = Oauth(
            id = null,
            user = naverUser,
            oauthId = "testNaverUser",
            oauthType = OauthTypeEnum.NAVER,
            accessToken = null
        )

        private val googleOauth = Oauth(
            id = null,
            user = googleUser,
            oauthId = "testGoogleUser",
            oauthType = OauthTypeEnum.GOOGLE,
            accessToken = null
        )
        @JvmStatic
        @BeforeAll
        internal fun beforeAll(
            @Autowired userRepository: UserRepository,
            @Autowired oauthRepository: OauthRepository
        ) {
            userRepository.saveAll(listOf(kakaoUser, naverUser, googleUser))
            logger.info("SAVE: {}", kakaoUser.toString())
            oauthRepository.saveAll(listOf(kakaoOauth, naverOauth, googleOauth))
        }

        @JvmStatic
        @AfterAll
        internal fun afterAll(
            @Autowired userRepository: UserRepository,
            @Autowired oauthRepository: OauthRepository
        ) {
            oauthRepository.deleteAll(listOf(kakaoOauth, naverOauth, googleOauth))
            userRepository.deleteAll(listOf(kakaoUser, naverUser, googleUser))
        }
    }

    @BeforeEach
    fun beforeEach() {

    }

    @AfterEach
    fun afterEach() {
        userRepository.saveAll(
        listOf(kakaoUser, naverUser, googleUser).map{
            it.registerState = RegisterStateEnum.INACTIVE
            it
        }.toList())
    }


    @Test
    fun isChangedRegisterState(){

        listOf(kakaoUser, naverUser, googleUser).map{
            //given
            val securityContextHolder = SecurityContextHolder.getContext()
            securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
                it.id, null, listOf(
                    SimpleGrantedAuthority("USER")))

            //when
            val postRegisterReq = authService.postRegister(PostRegisterReq(it.studentId.toString(), "test"), securityContextHolder.authentication)

            //then
            val user = userRepository.findById(it.id ?: throw NullPointerException("User Not Found")).orElseThrow ()
            assert(user.registerState == RegisterStateEnum.ACTIVE)
            assert(postRegisterReq.statusCode == HttpStatus.OK)
        }
    }
}