package cobo.writing.presentation

import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.data.dto.student.StudentGetListRes
import cobo.writing.data.dto.student.StudentGetRes
import cobo.writing.data.dto.student.StudentPostFeedBackReq
import cobo.writing.data.dto.student.StudentPostReq
import cobo.writing.service.AssignmentService
import cobo.writing.service.WritingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
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
        ApiResponse(responseCode = "201", description = "저장 성공, 업데이트 성공"),
        ApiResponse(responseCode = "400", description = "잘못된 state 요청, 기한 이외의 과제 제출"),
        ApiResponse(responseCode = "404", description = "해당 과제가 존재하지 않음")
    )
    fun post(@RequestBody studentPostReq: StudentPostReq, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>{
        return writingService.studentPost(studentPostReq, authentication)
    }

    @GetMapping
    @Operation(summary = "학생이 본인이 제출했던 과제를 조회하는 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "OK"),
        ApiResponse(responseCode = "404", description = "해당 과제가 존재하지 않음")
    )
    fun get(@RequestParam assignmentId: Int, @Parameter(hidden = true) authentication: Authentication):ResponseEntity<CoBoResponseDto<StudentGetRes>> {
        return writingService.studentGet(assignmentId, authentication)
    }

    @PostMapping("/feedback")
    @Operation(summary = "학생이 피드백을 요청하는 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "OK")
    )
    fun postFeedback(@RequestBody studentPostFeedBackReq: StudentPostFeedBackReq): ResponseEntity<CoBoResponseDto<StudentPostFeedBackReq>>{
        return writingService.postFeedback(studentPostFeedBackReq)
    }
}