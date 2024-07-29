package cobo.chat.presentation

import cobo.chat.config.response.CoBoResponse
import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.config.response.CoBoResponseStatus
import cobo.chat.data.dto.prof.ProfGetListRes
import cobo.chat.data.dto.prof.ProfGetRes
import cobo.chat.data.dto.prof.ProfPostReq
import cobo.chat.data.enum.ChatStateEnum
import cobo.chat.service.ChatService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime


@RestController
@RequestMapping("/api/prof")
class ProfPresentation(
    private val chatService: ChatService
) {

    @GetMapping
    @Operation(summary = "학생 질문 조회")
    @Parameters(
        Parameter(name = "studentId", description = "조회할 학생의 학번")
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "조회 성공")
    )
    fun get(@RequestParam studentId: String): ResponseEntity<CoBoResponseDto<List<ProfGetRes>>>{
        return CoBoResponse(
            listOf(
                ProfGetRes(
                    comment = "Hello1", localDateTime = LocalDateTime.now(), chatStateEnum = ChatStateEnum.COMPLETE),
                ProfGetRes(
                    comment = "Hello2", localDateTime = LocalDateTime.now(), chatStateEnum = ChatStateEnum.CONFIRMATION)
            ),CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    @GetMapping("/list")
    @Operation(summary = "학생들의 질문 리스트 조회")
    @Parameters(
        Parameter(name = "page", description = "페이지 번호", example = "0"),
        Parameter(name = "pageSize", description = "한 페이지의 요소 개수", example = "10")
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "조회 성공")
    )
    fun getList(@RequestParam page: Int, @RequestParam pageSize: Int): ResponseEntity<CoBoResponseDto<ProfGetListRes>> {
        return chatService.profGetList(page, pageSize)
    }

    @PostMapping
    @Operation(summary = "교수의 답변 작성")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "404", description = "일치하는 학번이 존재하지 않음"),
    )
    fun post(@RequestBody profPostReq: ProfPostReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        return chatService.profPost(profPostReq)
    }

}