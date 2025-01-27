package cobo.auth.auth

import cobo.auth.config.jwt.JwtTokenProvider
import cobo.auth.data.dto.auth.PostAuthRegisterReq
import cobo.auth.data.entity.Oauth
import cobo.auth.data.entity.StudentInfo
import cobo.auth.data.entity.User
import cobo.auth.data.enums.OauthTypeEnum
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import cobo.auth.repository.OauthRepository
import cobo.auth.repository.StudentInfoRepository
import cobo.auth.repository.UserRepository
import cobo.auth.service.AuthService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.annotation.DirtiesContext
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthPostRegisterTest(
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val authService: AuthService,
    @Autowired private val oauthRepository: OauthRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val studentInfoRepository: StudentInfoRepository
) {

    private final val kakaoStudentId = UUID.randomUUID().toString().substring(0, 10)
    private final val naverStudentId = UUID.randomUUID().toString().substring(0, 10)

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


    @BeforeEach
    fun init(){
        userRepository.saveAll(listOf(kakaoUser, naverUser))
        oauthRepository.saveAll(listOf(kakaoOauth, naverOauth))
        studentInfoRepository.saveAll(
            listOf(
                StudentInfo(id = null, studentId = kakaoStudentId, name = "KAKAO"),
                StudentInfo(id = null, studentId = naverStudentId, name = "NAVER")
            )
        )
    }


    @AfterEach
    fun afterEach() {
        kakaoOauth.user = null
        naverOauth.user = null
        oauthRepository.deleteAll(listOf(kakaoOauth, naverOauth))
        userRepository.deleteAll(listOf(kakaoUser, naverUser))
        studentInfoRepository.deleteByStudentIdAndName(studentId = kakaoStudentId, name = "KAKAO")
        studentInfoRepository.deleteByStudentIdAndName(studentId = naverStudentId, name = "NAVER")
    }


    @Test
    fun isChangedRegisterState(){

        listOf(kakaoUser, naverUser).map{
            //given
            val securityContextHolder = SecurityContextHolder.getContext()
            securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
                it.id, null, listOf(
                    SimpleGrantedAuthority("USER")))

            //when

            val postAuthRegisterReq = authService.postRegister(
                PostAuthRegisterReq(studentId = if(it == kakaoUser){
                    kakaoStudentId
                }else{
                    naverStudentId
                }
                    , name =  if(it == kakaoUser){
                    "KAKAO"
                }else{
                    "NAVER"
                }),
                securityContextHolder.authentication)

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
    fun combineWithNaverAndKakao(){
        combineTwoSocial(naverUser, kakaoUser)
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


        val sameStudentId = kakaoStudentId

        val postAuthRegisterReq1 = authService.postRegister(PostAuthRegisterReq(sameStudentId, "KAKAO"), securityContextHolder1.authentication)
        val postAuthRegisterReq2 = authService.postRegister(PostAuthRegisterReq(sameStudentId, "KAKAO"), securityContextHolder2.authentication)

        //then
        val findUser1 = userRepository.findById(kakaoUser.id ?: throw NullPointerException("User Not Found")).orElseThrow()
        val findUser2 = userRepository.findById(naverUser.id ?: throw NullPointerException("User Not Found"))

        assert(findUser2.isEmpty)
        assert(findUser1.registerState == RegisterStateEnum.ACTIVE)
        assert(findUser1.studentId == sameStudentId)

        assert(postAuthRegisterReq1.statusCode == HttpStatus.OK)
        assert(postAuthRegisterReq2.statusCode == HttpStatus.OK)

        assert(jwtTokenProvider.getId(postAuthRegisterReq1.body?.data?.accessToken ?: "").toInt() == kakaoUser.id)
        assert(jwtTokenProvider.getId(postAuthRegisterReq2.body?.data?.accessToken ?: "").toInt() == kakaoUser.id)
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

        val sameStudentId = naverStudentId

        //when
        val postAuthRegisterReq1 = authService.postRegister(PostAuthRegisterReq(sameStudentId, "NAVER"), securityContextHolder1.authentication)
        val postAuthRegisterReq2 = authService.postRegister(PostAuthRegisterReq(sameStudentId, "NAVER"), securityContextHolder2.authentication)

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

        assert(jwtTokenProvider.getStudentId(postAuthRegisterReq1.body?.data?.accessToken!!) == sameStudentId)
        assert(jwtTokenProvider.getStudentId(postAuthRegisterReq2.body?.data?.accessToken!!) == sameStudentId)

        assertEquals(jwtTokenProvider.getRole(postAuthRegisterReq1.body?.data?.accessToken!!), user1.role)
        assertEquals(jwtTokenProvider.getRole(postAuthRegisterReq2.body?.data?.accessToken!!), user1.role)
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

    @Test
    fun invalidStudentRegister(){
        //given
        val securityContextHolder = SecurityContextHolder.getContext()
        securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
            kakaoUser.id, null, listOf(
                SimpleGrantedAuthority("USER")))



        //when
        try{
            authService.postRegister(
                PostAuthRegisterReq(kakaoUser.id.toString(), UUID.randomUUID().toString().substring(0, 10)), securityContextHolder.authentication)
            assert(false)
        }
        catch(nullPointerException:NullPointerException){
            assert(true)
        }
        finally {

            val user:User = userRepository.findById(kakaoUser.id ?: throw NullPointerException("User Not Found")).orElseThrow()

            assertNull(user.studentId)
            assertEquals(RegisterStateEnum.INACTIVE, user.registerState)
        }
    }
}