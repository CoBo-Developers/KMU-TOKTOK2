package cobo.writing.presentation

import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.data.dto.assignment.AssignmentGetListRes
import cobo.writing.data.dto.assignment.AssignmentPostReq
import cobo.writing.service.AssignmentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/assignment")
class AssignmentPresentation(
    private val assignmentService: AssignmentService
) {

    @PostMapping
    @Operation(summary = "과제 생성")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "성공"),
    )
    fun post(
        @RequestBody assignmentPostReq: AssignmentPostReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>{
        return assignmentService.post(assignmentPostReq)
    }

    @GetMapping("/list")
    @Operation(summary = "과제 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공")
    )
    fun get(): ResponseEntity<CoBoResponseDto<AssignmentGetListRes>> {
        return assignmentService.getList()
    }

}