package cobo.writing.service.impl

import cobo.writing.config.response.CoBoResponse
import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.data.dto.student.StudentGetRes
import cobo.writing.data.dto.student.StudentPostReq
import cobo.writing.data.entity.Assignment
import cobo.writing.data.entity.Writing
import cobo.writing.data.enums.WritingStateEnum
import cobo.writing.repository.AssignmentRepository
import cobo.writing.repository.WritingRepository
import cobo.writing.service.WritingService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class WritingServiceImpl(
    private val writingRepository : WritingRepository,
    private val assignmentRepository : AssignmentRepository
): WritingService {

    override fun studentPost(
        studentPostReq: StudentPostReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {

        if(studentPostReq.writingState != WritingStateEnum.SUBMITTED.value){
            return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.BAD_STATE_REQUEST).getResponseEntityWithLog()
        }

        val studentId = authentication.name

        val optionalAssignment = assignmentRepository.findById(studentPostReq.assignmentId)

        val today = LocalDate.now()

        if(optionalAssignment.isEmpty){
            return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.NOT_FOUND_DATA).getResponseEntityWithLog()
        }
        else if(
            today.isBefore(optionalAssignment.get().startDate) ||
            today.isAfter(optionalAssignment.get().endDate)
        ){
            return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.EXPIRED_ASSIGNMENT).getResponseEntityWithLog()
        }

        val writing = Writing(
            id = null,
            studentId = studentId,
            assignment = optionalAssignment.get(),
            content = studentPostReq.content,
            state = WritingStateEnum.from(studentPostReq.writingState)!!,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            submittedAt = LocalDateTime.now(),
        )

        return if(writingRepository.ifExistsUpdateElseInsert(writing) == 1){
            CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.CREATED).getResponseEntity()
        } else{
            CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.UPDATED).getResponseEntityWithLog()
        }

    }

    override fun studentGet(
        assignmentId: Int,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<StudentGetRes>> {

        val studentId = authentication.name

        val assignment = Assignment(id = assignmentId)

        val writing = writingRepository.findByAssignmentAndStudentId(
            assignment = assignment,
            studentId = studentId
        )

        val studentGetRes = StudentGetRes(
            assignmentId = assignmentId,
            content = if(writing.isPresent) writing.get().content else ""
        )

        return CoBoResponse(studentGetRes, CoBoResponseStatus.SUCCESS).getResponseEntity()
    }
}