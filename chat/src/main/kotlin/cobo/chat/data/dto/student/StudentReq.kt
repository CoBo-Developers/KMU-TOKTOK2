package cobo.chat.data.dto.student

import com.fasterxml.jackson.annotation.JsonProperty

data class StudentPostReq(
    @JsonProperty("question")
    val question: String
)