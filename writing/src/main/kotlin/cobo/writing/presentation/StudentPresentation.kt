package cobo.writing.presentation

import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.data.dto.student.StudentGetListRes
import cobo.writing.data.dto.student.StudentPostReq
import cobo.writing.service.AssignmentService
import cobo.writing.service.WritingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

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
        return assignmentService.studentGetList(authentication)
    }

    @PostMapping
    @Operation(summary = "학생이 본인의 과제를 제출하는 API")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "OK"),
    )
    fun post(@RequestBody studentPostReq: StudentPostReq, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>{
        return writingService.studentPost(studentPostReq, authentication)
    }
}