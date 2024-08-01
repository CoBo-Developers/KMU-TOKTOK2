package cobo.chat.config

import cobo.chat.data.dto.chatGPT.ChatGPTReq
import cobo.chat.data.dto.chatGPT.ChatGPTReqMessage
import cobo.chat.data.dto.chatGPT.ChatGPTRes
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ServerErrorException

@Component
class ChatGPTConfig(
    @Value("\${chatGPT.api-key}")
    private val apiKey: String
) {

    private val chatGPTUrl = "https://api.openai.com/v1/chat/completions"


    fun requestChatGPT(content: String): ChatGPTRes?{
        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
        httpHeaders.contentType = MediaType.APPLICATION_JSON

        val chatGPTReqMessage = ChatGPTReqMessage(
            role = "user",
            content = content
        )

        val chatGPTReq = ChatGPTReq(
            model = "gpt-3.5-turbo-0125",
            messages = listOf(chatGPTReqMessage),
            stream = false
        )

        val response = restTemplate.exchange(
            chatGPTUrl, HttpMethod.POST, HttpEntity(chatGPTReq, httpHeaders), ChatGPTRes::class.java)

        return response.body
    }
}