package cobo.writing.presentation

import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.data.dto.student.StudentGetListRes
import cobo.writing.service.AssignmentService
import cobo.writing.service.WritingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/student")
class StudentPresentation(
    private val writingService: WritingService,
    private val assignmentService: AssignmentService
) {

    @GetMapping("/list")
    @Operation(summary = "학생이 과제 목록과 본인의 제출 상태를 가져오는 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "OK"),
    )
    fun getList(@Parameter(hidden = true) authentication: Authentication): ResponseEntity<CoBoResponseDto<StudentGetListRes>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}