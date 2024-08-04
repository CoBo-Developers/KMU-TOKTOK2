package cobo.writing.service.impl

import cobo.writing.repository.AssignmentRepository
import cobo.writing.service.AssignmentService
import org.springframework.stereotype.Service

@Service
class AssignmentServiceImpl(
    private val assignmentRepository: AssignmentRepository
): AssignmentService {
}