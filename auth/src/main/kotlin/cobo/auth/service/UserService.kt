package cobo.auth.service

import cobo.auth.config.response.CoBoResponseDto
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.data.dto.user.GetUserListRes
import cobo.auth.data.dto.user.PatchUserReq
import org.springframework.http.ResponseEntity

interface UserService {
    fun getList(page: Int, pageSize: Int): ResponseEntity<CoBoResponseDto<GetUserListRes>>
    fun patch(patchUserReq: PatchUserReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
}