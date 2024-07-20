package cobo.auth.service.impl

import cobo.auth.config.response.CoBoResponse
import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.data.dto.user.GetUserListRes
import cobo.auth.repository.UserRepository
import cobo.auth.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService{
    override fun getList(page: Int, pageSize: Int): ResponseEntity<CoBoResponseDto<GetUserListRes>> {
        val pageUser = userRepository.findAll(PageRequest.of(page, pageSize))
        return CoBoResponse(
            GetUserListRes(
                users = pageUser.toList(),
                totalElements = pageUser.totalElements
            ), CoBoResponseStatus.SUCCESS).getResponseEntityWithLog()
    }
}