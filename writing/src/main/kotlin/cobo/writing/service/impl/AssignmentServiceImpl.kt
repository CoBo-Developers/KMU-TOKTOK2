package cobo.writing.service.impl

import cobo.writing.config.response.CoBoResponse
import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.data.dto.professor.AssignmentGetListElementRes
import cobo.writing.data.dto.professor.AssignmentGetListRes
import cobo.writing.data.dto.professor.AssignmentPostReq
import cobo.writing.data.dto.professor.AssignmentPutReq
import cobo.writing.data.dto.student.StudentGetListRes
import cobo.writing.data.dto.student.StudentGetListResElement
import cobo.writing.data.entity.Assignment
import cobo.writing.data.enums.WritingStateEnum
import cobo.writing.repository.AssignmentRepository
import cobo.writing.repository.WritingRepository
import cobo.writing.service.AssignmentService
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AssignmentServiceImpl(
    private val assignmentRepository: AssignmentRepository,
    private val writingRepository: WritingRepository
): AssignmentService {

    override fun post(assignmentPostReq: AssignmentPostReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {

        if(assignmentPostReq.startDate.isAfter(assignmentPostReq.endDate)) {
            return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.INVALID_RANGE).getResponseEntity()
        }

        val assignment = Assignment(
            id = null,
            title = assignmentPostReq.title,
            description = assignmentPostReq.description,
            score = assignmentPostReq.score,
            startDate = assignmentPostReq.startDate,
            endDate = assignmentPostReq.endDate,
            prompt = assignmentPostReq.prompt,
        )

        assignmentRepository.save(assignment)

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.CREATED).getResponseEntity()
    }

    override fun put(assignmentPutReq: AssignmentPutReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        val assignment = assignmentRepository.findById(assignmentPutReq.id).orElseThrow { EntityNotFoundException("Could not find assignment with id ${assignmentPutReq.id}") }

        assignment.title = assignmentPutReq.title
        assignment.description = assignmentPutReq.description
        assignment.score = assignmentPutReq.score
        assignment.startDate = assignmentPutReq.startDate
        assignment.endDate = assignmentPutReq.endDate
        assignment.prompt = assignmentPutReq.prompt
        assignmentRepository.save(assignment)

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun getList(): ResponseEntity<CoBoResponseDto<AssignmentGetListRes>> {
        val assignmentGetListRes = AssignmentGetListRes(assignmentRepository.findAll().map{
            AssignmentGetListElementRes(
                id = it.id ?: 0,
                title = it.title ?: "",
                description = it.description ?: "",
                score = it.score!!,
                startDate = it.startDate!!,
                endDate = it.endDate!!,
                prompt = it.prompt!!,
            )
        })
        return CoBoResponse(assignmentGetListRes, CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun studentGetList(authentication: Authentication): ResponseEntity<CoBoResponseDto<StudentGetListRes>> {

        val studentId = authentication.name

        val writingList = writingRepository.findByStudentIdWithJDBC(studentId).associateBy { it.assignment.id!! }

        val assignments = assignmentRepository.findAll().map {
            val writing = writingList[it.id]

            val studentGetListResElement = StudentGetListResElement(
                id = it.id!!,
                title = it.title ?: "",
                description = it.description ?: "",
                score = it.score ?: 0,
                startDate = it.startDate ?: LocalDate.now(),
                endDate = it.endDate ?: LocalDate.now(),
                writingState = WritingStateEnum.NOT_SUBMITTED.value,
                writingScore = 0
            )

            if(writing != null){
                studentGetListResElement.writingState = writing.state.value
                studentGetListResElement.writingScore = writing.score ?: 0
            }

            studentGetListResElement
        }

        return CoBoResponse(
            StudentGetListRes(
                assignmentList = assignments,
            ), CoBoResponseStatus.SUCCESS
        ).getResponseEntity()
    }

    override fun delete(id: Int): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {

        val assignment = assignmentRepository.findById(id).orElseThrow { EntityNotFoundException("Could not find assignment with id $id") }

        assignmentRepository.delete(assignment)

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }
}