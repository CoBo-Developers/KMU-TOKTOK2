package cobo.chat.presentation

import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.data.dto.chatBot.ChatBotGetElementRes
import cobo.chat.data.dto.chatBot.ChatBotPostReq
import cobo.chat.data.dto.chatBot.ChatBotPostRes
import cobo.chat.service.ChatBotService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatBotPresentation(
    private val chatBotService: ChatBotService
) {

    @PostMapping
    @Operation(summary = "챗봇에 질문 작성")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "등록 성공")
    )
    fun post(@RequestBody chatBotPostReq: ChatBotPostReq, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<CoBoResponseDto<ChatBotPostRes>> {
        return chatBotService.post(chatBotPostReq, authentication)
    }

    @GetMapping
    @Operation(summary = "챗봇에 질문 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "조회 성공")
    )
    fun get(@Parameter(hidden = true) authentication: Authentication): ResponseEntity<CoBoResponseDto<List<ChatBotGetElementRes>>>{
        return chatBotService.get(authentication)
    }

    @GetMapping("/student")
    @Operation(summary = "해당 학생 챗봇 질문 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "조회 성공")
    )
    @Parameters(
        Parameter(name = "studentId", description = "학생의 학번")
    )
    fun get(@RequestParam studentId: String): ResponseEntity<CoBoResponseDto<List<ChatBotGetElementRes>>>{
        return chatBotService.get(studentId)
    }
}