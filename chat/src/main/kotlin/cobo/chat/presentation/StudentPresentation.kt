package cobo.chat.presentation

import cobo.chat.config.response.CoBoResponse
import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.config.response.CoBoResponseStatus
import cobo.chat.data.dto.student.StudentPostReq
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/student")
class StudentPresentation(

){

    @PostMapping
    @Operation(summary = "학생이 교수에게 질문 작성")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "작성 성공")
    )
    fun post(
        @RequestBody studentPostReq: StudentPostReq,
        @Parameter(hidden = true) authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>{
        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    
}