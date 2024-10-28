package cobo.writing.config

import cobo.writing.data.dto.chatGPT.AssistantReq
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
    @Value("\${open-ai.assistant_id}")
    private val assistantId: String,
    @Value("\${open-ai.model}")
    private val model: String,
    @Value("\${open-ai.role}")
    private val role: String,
    @Value("\${open-ai.fast-api}")
    private val fastApiUrl: String
) {

    private val chatGPTUrl = "https://api.openai.com/v1/chat/completions"


    fun requestChatGPT(content: String): ChatGPTRes?{
        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
        httpHeaders.contentType = MediaType.APPLICATION_JSON

        val chatGPTReqMessage = ChatGPTReqMessage(
            role = role,
            content = content
        )

        val chatGPTReq = ChatGPTReq(
            model = model,
            messages = listOf(chatGPTReqMessage),
            stream = false
        )

        val response = restTemplate.exchange(
            chatGPTUrl, HttpMethod.POST, HttpEntity(chatGPTReq, httpHeaders), ChatGPTRes::class.java)

        return response.body
    }

    fun requestAssistant(content: String): String{
        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON

        val assistantReq = AssistantReq(
            content = content,
            assistantId = assistantId,
        )

        val response = restTemplate.exchange(
            fastApiUrl, HttpMethod.POST, HttpEntity(assistantReq, httpHeaders), String::class.java)

        return response.body!!
    }
}