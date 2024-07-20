package cobo.auth.service.impl

import cobo.auth.config.LogFilter
import cobo.auth.config.jwt.JwtTokenProvider
import cobo.auth.config.response.CoBoResponse
import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.data.dto.auth.GetLoginRes
import cobo.auth.data.dto.auth.PostRegisterReq
import cobo.auth.data.entity.User
import cobo.auth.data.enums.OauthTypeEnum
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import cobo.auth.repository.OauthRepository
import cobo.auth.repository.UserRepository
import cobo.auth.service.AuthService
import cobo.auth.service.oauth.impl.GoogleOauthServiceImpl
import cobo.auth.service.oauth.impl.KakaoOauthServiceImpl
import cobo.auth.service.oauth.impl.NaverOauthServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class AuthServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val oauthRepository: OauthRepository,
    private val kakaoOauthServiceImpl: KakaoOauthServiceImpl,
    private val googleOauthServiceImpl: GoogleOauthServiceImpl,
    private val naverOauthServiceImpl: NaverOauthServiceImpl
) : AuthService {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LogFilter::class.java)
    }

    override fun getKakaoLogin(
        code: String
    ): ResponseEntity<CoBoResponseDto<GetLoginRes>> {

        val user = getUserByOauthCode(code, OauthTypeEnum.KAKAO, true)

        val tokenList = getAccessTokenAndRefreshTokenByUser(user)

        val coBoResponse = CoBoResponse(GetLoginRes(tokenList[0], tokenList[1], user.registerState), CoBoResponseStatus.SUCCESS)

        return coBoResponse.getResponseEntityWithLog()
    }

    override fun getNaverLogin(code: String): ResponseEntity<CoBoResponseDto<GetLoginRes>> {

        val user = getUserByOauthCode(code, OauthTypeEnum.NAVER, true)

        val tokenList = getAccessTokenAndRefreshTokenByUser(user)

        val coBoResponse = CoBoResponse(GetLoginRes(tokenList[0], tokenList[1], user.registerState), CoBoResponseStatus.SUCCESS)

        return coBoResponse.getResponseEntityWithLog()
    }

    override fun getGoogleLogin(code: String): ResponseEntity<CoBoResponseDto<GetLoginRes>> {
        val user = getUserByOauthCode(code, OauthTypeEnum.GOOGLE, true)

        val tokenList = getAccessTokenAndRefreshTokenByUser(user)

        val coBoResponse = CoBoResponse(GetLoginRes(tokenList[0], tokenList[1], user.registerState), CoBoResponseStatus.SUCCESS)

        return coBoResponse.getResponseEntityWithLog()
    }

    override fun postRegister(
        postRegisterReq: PostRegisterReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<GetLoginRes>> {

        val userId = authentication.name.toInt()

        if (userRepository.findById(userId).orElseThrow().registerState == RegisterStateEnum.ACTIVE)
            throw IllegalAccessException("ALREADY_REGISTERED")


        val user = userRepository.findByStudentIdWithJDBC(postRegisterReq.studentId)

        val tokenList: Array<String>

        if(user.isPresent){
            oauthRepository.updateUserIdByUserIdWithJDBC(
                oldUserId = userId,
                newUserId = user.get().id ?: userId)
            userRepository.deleteById(userId)
            tokenList = getAccessTokenAndRefreshTokenByUser(user.get())
        }
        else{
            userRepository.updateStudentIdWithJDBC(
                id = userId,
                studentId = postRegisterReq.studentId,
                registerStateEnum = RegisterStateEnum.ACTIVE
            )

            tokenList = getAccessTokenAndRefreshTokenByUser(User(userId))
        }

        val coBoResponse = CoBoResponse(GetLoginRes(tokenList[0], tokenList[1], RegisterStateEnum.ACTIVE), CoBoResponseStatus.SUCCESS)

        return coBoResponse.getResponseEntityWithLog()
    }

    override fun getKakaoLocalLogin(code: String): ResponseEntity<CoBoResponseDto<GetLoginRes>> {
        val user = getUserByOauthCode(code, OauthTypeEnum.KAKAO, false)

        val tokenList = getAccessTokenAndRefreshTokenByUser(user)

        val coBoResponse = CoBoResponse(GetLoginRes(tokenList[0], tokenList[1], user.registerState), CoBoResponseStatus.SUCCESS)

        return coBoResponse.getResponseEntityWithLog()
    }

    override fun getGoogleLocalLogin(code: String): ResponseEntity<CoBoResponseDto<GetLoginRes>> {
        val user = getUserByOauthCode(code, OauthTypeEnum.GOOGLE, false)

        val tokenList = getAccessTokenAndRefreshTokenByUser(user)

        val coBoResponse = CoBoResponse(GetLoginRes(tokenList[0], tokenList[1], user.registerState), CoBoResponseStatus.SUCCESS)

        return coBoResponse.getResponseEntityWithLog()
    }

    private fun getUserByOauthCode(code: String, oauthTypeEnum: OauthTypeEnum, isRemote: Boolean): User {
        val oauth = when(oauthTypeEnum) {
            OauthTypeEnum.KAKAO -> kakaoOauthServiceImpl.getOauth(code, isRemote)
            OauthTypeEnum.NAVER -> naverOauthServiceImpl.getOauth(code, isRemote)
            OauthTypeEnum.GOOGLE -> googleOauthServiceImpl.getOauth(code, isRemote)
        }

        if (oauth.user != null) {
            return oauth.user ?: throw NullPointerException()
        } else{
            val user = userRepository.save(
                User(
                    id = null,
                    studentId = null,
                    role = RoleEnum.STUDENT,
                    registerState = RegisterStateEnum.INACTIVE
                )
            )
            CompletableFuture.runAsync {
                oauth.user = user
                oauthRepository.save(oauth)
            }.exceptionally {
                logger.error("Failed to save user {} {}", code, oauthTypeEnum.name)
                null
            }
            return user
        }
    }

    private fun getAccessTokenAndRefreshTokenByUser(user: User): Array<String>{
        return arrayOf(
            jwtTokenProvider.getAccessToken(user.id ?: throw NullPointerException()),
            jwtTokenProvider.getRefreshToken(user.id ?: throw NullPointerException())
        )
    }

}