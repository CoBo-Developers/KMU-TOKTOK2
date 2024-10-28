package cobo.writing.data.dto.chatGPT

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatGPTRes(
    @JsonProperty("id")
    val id: String,

    @JsonProperty("object")
    val objectName: String,

    @JsonProperty("created")
    val created: Long,

    @JsonProperty("model")
    val model: String,

    @JsonProperty("choices")
    val choices: List<Choice>,

    @JsonProperty("usage")
    val usage: Usage,

    @JsonProperty("system_fingerprint")
    val systemFingerprint: String?,
)

data class Choice(
    @JsonProperty("index")
    val index: Int,

    @JsonProperty("message")
    val message: ChatGPTResMessage,

    @JsonProperty("logprobs")
    val logprobs: String?,

    @JsonProperty("finish_reason")
    val finishReason: String,
)

data class ChatGPTResMessage(
    @JsonProperty("role")
    val role: String,

    @JsonProperty("content")
    val content: String,
)

data class Usage(
    @JsonProperty("prompt_tokens")
    val promptTokens: Int,

    @JsonProperty("completion_tokens")
    val completionTokens: Int,

    @JsonProperty("total_tokens")
    val totalTokens: Int
)