package cobo.auth.service.impl

import cobo.auth.config.response.CoBoResponse
import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.data.dto.user.GetUserListRes
import cobo.auth.data.dto.user.PutUserReq
import cobo.auth.data.dto.user.GetUserRes
import cobo.auth.data.entity.User
import cobo.auth.repository.UserRepository
import cobo.auth.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService{
    override fun getList(page: Int, pageSize: Int): ResponseEntity<CoBoResponseDto<GetUserListRes>> {
        val pageUser: Page<User> = userRepository.findByStudentIdNotNullOrderByStudentIdAsc(PageRequest.of(page, pageSize))
        return CoBoResponse(
            GetUserListRes(
                users = pageUser.toList().map{
                    GetUserRes(it)
                },
                totalElements = pageUser.totalElements
            ), CoBoResponseStatus.SUCCESS).getResponseEntityWithLog()
    }

    override fun get(studentId: String): ResponseEntity<CoBoResponseDto<GetUserRes>> {
        val user = userRepository.findByStudentId(studentId).orElseThrow {NullPointerException()}
        return CoBoResponse(GetUserRes(
            studentId = user.studentId,
            role = user.role.name,
            registerState = user.registerState.name,
        ), CoBoResponseStatus.SUCCESS).getResponseEntityWithLog()
    }

    override fun put(putUserReq: PutUserReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return CoBoResponse<CoBoResponseStatus>(
            if (userRepository.updateUserByStudentIdWithJDBC(
                    studentId = putUserReq.studentId,
                    role = putUserReq.role,
                    registerState = putUserReq.registerState) > 0)
                CoBoResponseStatus.SUCCESS
            else
                CoBoResponseStatus.NO_DATA_CHANGES).getResponseEntityWithLog()
    }
}