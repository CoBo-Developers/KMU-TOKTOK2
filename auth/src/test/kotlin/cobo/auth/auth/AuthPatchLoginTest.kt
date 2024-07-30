package cobo.auth.auth

import cobo.auth.config.jwt.JwtTokenProvider
import cobo.auth.data.entity.User
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import cobo.auth.repository.UserRepository
import cobo.auth.service.AuthService
import io.jsonwebtoken.MalformedJwtException
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class AuthPatchLoginTest(
    @Autowired private val authService: AuthService,
    @Autowired private val jwtTokenProvider: JwtTokenProvider
) {

    companion object{
        private val user = User(
            id = null,
            studentId = "0123456789",
            role = RoleEnum.STUDENT,
            registerState = RegisterStateEnum.ACTIVE,
        )

        @JvmStatic
        @BeforeAll
        internal fun beforeAll(
            @Autowired userRepository: UserRepository
        ) {
            userRepository.save(user)
        }

        @JvmStatic
        @AfterAll
        internal fun afterAll(
            @Autowired userRepository: UserRepository
        ) {
            userRepository.delete(user)
        }
    }

    @Test
    fun getTokenWithValidUser(){
        //given
        val refreshToken = jwtTokenProvider.getRefreshToken(user.id ?: 0, null, user.role)

        //when
        val patchLoginRes = authService.patchLogin("Bearer $refreshToken")

        //then
        val newAccessToken = patchLoginRes.body!!.data!!.accessToken
        val newRefreshToken = patchLoginRes.body!!.data!!.refreshToken

        println(refreshToken)
        println(newAccessToken)
        assert(newRefreshToken == refreshToken)
        assert(user.id == jwtTokenProvider.getId(newAccessToken).toInt())
    }

    @Test
    fun getTokenWithNoBearer(){
        //given
        val randomString = UUID.randomUUID().toString()

        //when
        try{
            authService.patchLogin(randomString)
            assert(false)
        }catch (e: IndexOutOfBoundsException){
            //then
            assert(true)
        }
    }

    @Test
    fun getTokenWithInvalidUser(){
        //given
        val randomString = "${UUID.randomUUID()}.${UUID.randomUUID()}.${UUID.randomUUID()}"

        try{
            //when
            authService.patchLogin("Bearer $randomString")
            assert(false)
        }catch(_: MalformedJwtException){
            //then
            assert(true)
        }
    }




}