package cobo.writing.config

import cobo.writing.data.dto.chatGPT.ChatGPTReq
import cobo.writing.data.dto.chatGPT.ChatGPTReqMessage
import cobo.writing.data.dto.chatGPT.ChatGPTRes
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class ChatGPTConfig(
    @Value("\${open-ai.api-key}")
    private val apiKey: String,
    @Value("\${open-ai.model}")
    private val model: String
) {

    private val chatGPTUrl = "https://api.openai.com/v1/chat/completions"

    fun requestChatGPT(userContent: String, systemContent: String): ChatGPTRes?{
        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
        httpHeaders.contentType = MediaType.APPLICATION_JSON

        val chatGPTSystemMessage = ChatGPTReqMessage(
            role = "system",
            content = systemContent,
        )

        val chatGPTUserReqMessage = ChatGPTReqMessage(
            role = "user",
            content = userContent
        )

        val chatGPTReq = ChatGPTReq(
            model = model,
            messages = listOf(chatGPTSystemMessage, chatGPTUserReqMessage),
            stream = false
        )

        val response = restTemplate.exchange(
            chatGPTUrl, HttpMethod.POST, HttpEntity(chatGPTReq, httpHeaders), ChatGPTRes::class.java)

        return response.body
    }
}