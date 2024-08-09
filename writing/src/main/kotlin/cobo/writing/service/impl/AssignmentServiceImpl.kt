package cobo.writing.service.impl

import cobo.writing.config.response.CoBoResponse
import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.data.dto.professor.AssignmentGetListElementRes
import cobo.writing.data.dto.professor.AssignmentGetListRes
import cobo.writing.data.dto.professor.AssignmentPostReq
import cobo.writing.data.dto.professor.AssignmentPutReq
import cobo.writing.data.dto.student.StudentGetListRes
import cobo.writing.data.entity.Assignment
import cobo.writing.repository.AssignmentRepository
import cobo.writing.service.AssignmentService
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class AssignmentServiceImpl(
    private val assignmentRepository: AssignmentRepository
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
            endDate = assignmentPostReq.endDate
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
                endDate = it.endDate!!
            )
        })
        return CoBoResponse(assignmentGetListRes, CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun studentGetList(authentication: Authentication): ResponseEntity<CoBoResponseDto<StudentGetListRes>> {

        val studentId = authentication.name

        val assignmentList = assignmentRepository.findByUserWithJDBC(studentId)

        return CoBoResponse(
            StudentGetListRes(
                assignmentList = assignmentList,
            ), CoBoResponseStatus.SUCCESS
        ).getResponseEntity()
    }

    override fun delete(id: Int): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {

        val assignment = assignmentRepository.findById(id).orElseThrow { EntityNotFoundException("Could not find assignment with id $id") }

        assignmentRepository.delete(assignment)

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }
}