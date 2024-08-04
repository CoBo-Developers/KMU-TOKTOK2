package cobo.writing.service.impl

import cobo.writing.config.response.CoBoResponse
import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.data.dto.assignment.AssignmentPostReq
import cobo.writing.data.entity.Assignment
import cobo.writing.repository.AssignmentRepository
import cobo.writing.service.AssignmentService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AssignmentServiceImpl(
    private val assignmentRepository: AssignmentRepository
): AssignmentService {

    override fun post(assignmentPostReq: AssignmentPostReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        val assignment = Assignment(
            id = null,
            title = assignmentPostReq.title,
            description = assignmentPostReq.description,
            score = assignmentPostReq.score,
            startDate = assignmentPostReq.startDate,
            endDate = assignmentPostReq.endDate
        )

        assignmentRepository.save(assignment)

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }
}