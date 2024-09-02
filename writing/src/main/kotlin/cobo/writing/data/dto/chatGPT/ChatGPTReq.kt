package cobo.writing.data.dto.chatGPT

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatGPTReq(

    val model: String,

    val messages: List<ChatGPTReqMessage>,

    val stream: Boolean
)

data class ChatGPTReqMessage(
    val role: String,
    val content: String
)

data class AssistantReq(
    val content: String,
    @JsonProperty("assistant_id")
    val assistantId: String
)