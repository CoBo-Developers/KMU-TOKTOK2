package cobo.chat.data.dto.chatGPT

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
    val content: String
)