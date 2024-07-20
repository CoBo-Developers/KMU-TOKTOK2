package cobo.auth.service

import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.data.dto.user.GetUserListRes
import org.springframework.http.ResponseEntity

interface UserService {
    fun getList(page: Int, pageSize: Int): ResponseEntity<CoBoResponseDto<GetUserListRes>>
}