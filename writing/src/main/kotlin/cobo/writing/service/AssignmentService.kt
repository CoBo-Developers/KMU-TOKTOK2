package cobo.writing.service

import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.data.dto.professor.AssignmentGetListRes
import cobo.writing.data.dto.professor.AssignmentPostReq
import cobo.writing.data.dto.professor.AssignmentPutReq
import cobo.writing.data.dto.student.StudentGetListRes
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

interface AssignmentService {
    fun post(assignmentPostReq: AssignmentPostReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
    fun getList(): ResponseEntity<CoBoResponseDto<AssignmentGetListRes>>
    fun put(assignmentPutReq: AssignmentPutReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
    fun studentGetList(authentication: Authentication): ResponseEntity<CoBoResponseDto<StudentGetListRes>>
}