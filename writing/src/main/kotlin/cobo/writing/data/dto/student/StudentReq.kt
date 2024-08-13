package cobo.writing.data.dto.student

import io.swagger.v3.oas.annotations.media.Schema


data class StudentPostReq(
    @Schema(description = "제출하려는 과제의 ID")
    val assignmentId: Int,
    @Schema(description = "요청하는 글쓰기의 state")
    val writingState: Short,
    @Schema(description = "글쓰기 내용")
    val content: String
)

data class StudentPostFeedbackReq(
    @Schema(description = "과제의 아이디")
    val assignmentId: Int,

    @Schema(description = "학생이 작성한 글의 내용")
    val content: String
)