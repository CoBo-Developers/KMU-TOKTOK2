package cobo.auth.auth

import cobo.auth.config.jwt.JwtTokenProvider
import cobo.auth.data.dto.auth.PostAuthRegisterReq
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
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

@SpringBootTest
class AuthPostRegisterTest(
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val authService: AuthService,
    @Autowired private val oauthRepository: OauthRepository,
    @Autowired private val userRepository: UserRepository
) {

    companion object {

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
            oauthType = OauthTypeEnum.KAKAO
        )

        private val naverOauth = Oauth(
            id = null,
            user = naverUser,
            oauthId = "testNaverUser",
            oauthType = OauthTypeEnum.NAVER
        )

        private val googleOauth = Oauth(
            id = null,
            user = googleUser,
            oauthId = "testGoogleUser",
            oauthType = OauthTypeEnum.GOOGLE
        )
        @JvmStatic
        @BeforeAll
        internal fun beforeAll(
            @Autowired userRepository: UserRepository,
            @Autowired oauthRepository: OauthRepository
        ) {
            userRepository.saveAll(listOf(kakaoUser, naverUser, googleUser))
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

    @AfterEach
    fun afterEach() {
        listOf(kakaoUser, naverUser, googleUser).forEach { user ->
            user.studentId = null
            user.registerState = RegisterStateEnum.INACTIVE
            if (!(userRepository.existsById(user.id!!))){
                user.id = null
                userRepository.save(user)
                if(user.id != kakaoUser.id && user.id != naverUser.id)
                    googleUser.id = user.id
                else if(user.id != naverUser.id && user.id != googleUser.id)
                    kakaoUser.id = user.id
                else
                    naverUser.id = user.id
            }
            else{
                userRepository.save(user)
            }
        }

        listOf(kakaoOauth, naverOauth, googleOauth).forEach { oauth ->
            oauth.user = when (oauth.oauthType) {
                OauthTypeEnum.KAKAO -> kakaoUser
                OauthTypeEnum.NAVER -> naverUser
                OauthTypeEnum.GOOGLE -> googleUser
            }
            if(oauthRepository.findById(oauth.id ?: 0).isEmpty) {
                oauth.id = null
                when (oauth.oauthType) {
                    OauthTypeEnum.KAKAO -> {
                        kakaoOauth.id = oauth.id
                        oauth.user = kakaoUser
                    }
                    OauthTypeEnum.NAVER -> {
                        naverOauth.id = oauth.id
                        oauth.user = naverUser
                    }
                    OauthTypeEnum.GOOGLE -> {
                        googleOauth.id = oauth.id
                        oauth.user = googleUser
                    }
                }
            }
            println(oauth)
            oauthRepository.save(oauth)
        }
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
            val postAuthRegisterReq = authService.postRegister(PostAuthRegisterReq(it.id.toString(), "test"), securityContextHolder.authentication)

            //then
            val user = userRepository.findById(it.id ?: throw NullPointerException("User Not Found")).orElseThrow ()
            assert(user.registerState == RegisterStateEnum.ACTIVE)
            assert(postAuthRegisterReq.statusCode == HttpStatus.OK)
        }
    }

    @Test
    fun combineWithKakaoAndNaver(){
        //given
        combineTwoSocial(kakaoUser, naverUser)
    }

    @Test
    fun combineWithKakaoAndGoogle(){
        combineTwoSocial(kakaoUser, googleUser)
    }

    @Test
    fun combineWithNaverAndKakao(){
        combineTwoSocial(naverUser, kakaoUser)
    }

    @Test
    fun combineWithNaverAndGoogle(){
        combineTwoSocial(naverUser, googleUser)
    }

    @Test
    fun combineWithGoogleAndKakao(){
        combineTwoSocial(googleUser, kakaoUser)
    }

    @Test
    fun combineWithGoogleAndNaver(){
        combineTwoSocial(googleUser, naverUser)
    }


    @Test
    fun combineWithAllSocial(){
        val securityContextHolder1 = SecurityContextHolder.createEmptyContext()
        securityContextHolder1.authentication = UsernamePasswordAuthenticationToken(
            kakaoUser.id, null, listOf(
                SimpleGrantedAuthority("USER")))

        val securityContextHolder2 = SecurityContextHolder.createEmptyContext()
        securityContextHolder2.authentication = UsernamePasswordAuthenticationToken(
            naverUser.id, null, listOf(
                SimpleGrantedAuthority("USER")))

        val securityContextHolder3 = SecurityContextHolder.createEmptyContext()
        securityContextHolder3.authentication = UsernamePasswordAuthenticationToken(
            googleUser.id, null, listOf(
                SimpleGrantedAuthority("USER")
            )
        )

        val sameStudentId = "test_studentId"

        val postAuthRegisterReq1 = authService.postRegister(PostAuthRegisterReq(sameStudentId, "test"), securityContextHolder1.authentication)
        val postAuthRegisterReq2 = authService.postRegister(PostAuthRegisterReq(sameStudentId, "test"), securityContextHolder2.authentication)
        val postAuthRegisterReq3 = authService.postRegister(PostAuthRegisterReq(sameStudentId, "test"), securityContextHolder3.authentication)

        //then
        val findUser1 = userRepository.findById(kakaoUser.id ?: throw NullPointerException("User Not Found")).orElseThrow()
        val findUser2 = userRepository.findById(naverUser.id ?: throw NullPointerException("User Not Found"))
        val findUser3 = userRepository.findById(googleUser.id ?: throw NullPointerException("User Not Found"))

        assert(findUser2.isEmpty)
        assert(findUser3.isEmpty)
        assert(findUser1.registerState == RegisterStateEnum.ACTIVE)
        assert(findUser1.studentId == sameStudentId)

        assert(postAuthRegisterReq1.statusCode == HttpStatus.OK)
        assert(postAuthRegisterReq2.statusCode == HttpStatus.OK)
        assert(postAuthRegisterReq3.statusCode == HttpStatus.OK)

        assert(jwtTokenProvider.getId(postAuthRegisterReq1.body?.data?.accessToken ?: "").toInt() == kakaoUser.id)
        assert(jwtTokenProvider.getId(postAuthRegisterReq2.body?.data?.accessToken ?: "").toInt() == kakaoUser.id)
        assert(jwtTokenProvider.getId(postAuthRegisterReq3.body?.data?.accessToken ?: "").toInt() == kakaoUser.id)
    }

    fun combineTwoSocial(user1: User, user2: User){
        //given
        val securityContextHolder1 = SecurityContextHolder.createEmptyContext()
        securityContextHolder1.authentication = UsernamePasswordAuthenticationToken(
            user1.id, null, listOf(
                SimpleGrantedAuthority("USER")))

        val securityContextHolder2 = SecurityContextHolder.createEmptyContext()
        securityContextHolder2.authentication = UsernamePasswordAuthenticationToken(
            user2.id, null, listOf(
                SimpleGrantedAuthority("USER")))

        val sameStudentId = "test_studentId"


        //when
        val postAuthRegisterReq1 = authService.postRegister(PostAuthRegisterReq(sameStudentId, "test"), securityContextHolder1.authentication)
        val postAuthRegisterReq2 = authService.postRegister(PostAuthRegisterReq(sameStudentId, "test"), securityContextHolder2.authentication)

        //then
        val findUser1 = userRepository.findById(user1.id ?: throw NullPointerException("User Not Found")).orElseThrow()
        val findUser2 = userRepository.findById(user2.id ?: throw NullPointerException("User Not Found"))

        assert(findUser2.isEmpty)
        assert(findUser1.registerState == RegisterStateEnum.ACTIVE)
        assert(findUser1.studentId == sameStudentId)

        assert(postAuthRegisterReq1.statusCode == HttpStatus.OK)
        assert(postAuthRegisterReq2.statusCode == HttpStatus.OK)

        assert(jwtTokenProvider.getId(postAuthRegisterReq1.body?.data?.accessToken ?: "").toInt() == user1.id)
        assert(jwtTokenProvider.getId(postAuthRegisterReq2.body?.data?.accessToken ?: "").toInt() == user1.id)
    }

    @Test
    fun duplicateRequestError(){
        //given
        val copyKakaoUser = kakaoUser.copy()

        try{
            combineTwoSocial(kakaoUser, copyKakaoUser)
            assert(false)
        }catch(e: IllegalAccessException){
            assert(true)
        }

    }
}