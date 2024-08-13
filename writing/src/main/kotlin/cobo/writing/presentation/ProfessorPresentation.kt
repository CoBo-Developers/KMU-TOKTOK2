package cobo.writing.presentation

import cobo.writing.config.response.CoBoResponseDto
import cobo.writing.config.response.CoBoResponseStatus
import cobo.writing.data.dto.professor.*
import cobo.writing.service.AssignmentService
import cobo.writing.service.WritingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/professor")
class ProfessorPresentation(
    private val assignmentService: AssignmentService,
    private val writingService: WritingService
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

    @PutMapping
    @Operation(summary = "과제 수정")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공")
    )
    fun put(
        @RequestBody assignmentPutReq: AssignmentPutReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>{
        return assignmentService.put(assignmentPutReq)
    }

    @DeleteMapping
    @Operation(summary = "과제 삭제")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "404", description = "해당 데이터가 없음")
    )
    fun delete(
        @RequestParam id: Int
    ): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return assignmentService.delete(id)
    }

    @PatchMapping("/writing")
    @Operation(summary = "교수가 해당 글쓰기의 상태를 변경")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공")
    )
    fun patchWriting(
        @RequestBody assignmentPatchWritingReq: AssignmentPatchWritingReq
    ): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return writingService.assignmentPatchWriting(assignmentPatchWritingReq)
    }


    @GetMapping("/writing-list")
    @Operation(summary = "교수가 글쓰기 목록을 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공")
    )
    fun getWritingList(
        @RequestParam assignmentId: Int,
        @RequestParam page: Int,
        @RequestParam pageSize: Int
    ): ResponseEntity<CoBoResponseDto<ProfessorGetWritingListRes>> {
        return writingService.professorGetWritingList(assignmentId, page, pageSize)
    }

    @GetMapping("/writing")
    @Operation(summary = "교수가 해당 글쓰기를 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공")
    )
    fun getWriting(
        @RequestParam assignmentId: Int,
        @RequestParam studentId: String
    ): ResponseEntity<CoBoResponseDto<ProfessorGetWriting>>{
        TODO()
//        return writingService.professorGetWriting(assignmentId, studentId)
    }
}