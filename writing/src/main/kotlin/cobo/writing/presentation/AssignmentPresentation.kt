package cobo.writing.presentation

import cobo.writing.service.AssignmentService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/assignment")
class AssignmentPresentation(
    private val assignmentService: AssignmentService
) {


}