package cobo.writing.service

import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.data.dto.professor.AssignmentPatchWritingReq
import cobo.writing.data.dto.professor.ProfessorGetWriting
import cobo.writing.data.dto.professor.ProfessorGetWritingListRes
import cobo.writing.data.dto.student.StudentGetRes
import cobo.writing.data.dto.student.StudentPostFeedBackReq
import cobo.writing.data.dto.student.StudentPostReq
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

interface WritingService {
    fun studentPost(
        studentPostReq: StudentPostReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>

    fun studentGet(assignmentId: Int, authentication: Authentication): ResponseEntity<CoBoResponseDto<StudentGetRes>>
    fun assignmentPatchWriting(assignmentPatchWritingReq: AssignmentPatchWritingReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
    fun professorGetWritingList(
        assignmentId: Int,
        page: Int,
        pageSize: Int
    ): ResponseEntity<CoBoResponseDto<ProfessorGetWritingListRes>>

    fun professorGetWriting(assignmentId: Int, studentId: String): ResponseEntity<CoBoResponseDto<ProfessorGetWriting>>
    fun postFeedback(studentPostFeedBackReq: StudentPostFeedBackReq): ResponseEntity<CoBoResponseDto<StudentPostFeedBackReq>>
}