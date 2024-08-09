package cobo.writing.service

import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.data.dto.student.StudentGetRes
import cobo.writing.data.dto.student.StudentPostReq
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

interface WritingService {
    fun studentPost(
        studentPostReq: StudentPostReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>

    fun studentGet(assignmentId: Int, authentication: Authentication): ResponseEntity<CoBoResponseDto<StudentGetRes>>
}