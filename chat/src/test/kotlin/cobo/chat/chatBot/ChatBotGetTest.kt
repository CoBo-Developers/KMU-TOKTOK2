package cobo.chat.chatBot

import cobo.chat.data.entity.ChatBotChat
import cobo.chat.repository.ChatBotChatRepository
import cobo.chat.service.ChatBotService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ChatBotGetTest @Autowired constructor(
    private val chatBotChatRepository: ChatBotChatRepository,
    private val chatBotService: ChatBotService
) {

    private val testStudentId = "TEST_STUDENT_ID"

    private var startTime = System.currentTimeMillis()

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @BeforeEach
    fun init() {
        startTime = System.currentTimeMillis()
    }

    @AfterEach
    fun clear(){
        chatBotChatRepository.deleteByStudentId(studentId = testStudentId)
        logger.info("TIME: ${System.currentTimeMillis() - startTime}")
    }

    @Test
    fun testValidSize(){
        //given
        val chatBotChatList = mutableListOf<ChatBotChat>()
        val randomCount = (10..20).random()
        for (i in 1..randomCount) {
            chatBotChatList.add(ChatBotChat(
                    id = null,
                    studentId = testStudentId,
                    question = UUID.randomUUID().toString(),
                    answer = UUID.randomUUID().toString(),
                    createdAt = null
                ))
        }
        chatBotChatRepository.saveAll(chatBotChatList)

        //when
        val chatBotGetRes1 = chatBotService.get(studentId = testStudentId)
        chatBotChatRepository.save(
            ChatBotChat(
                id = null,
                studentId = testStudentId,
                question = UUID.randomUUID().toString(),
                answer = UUID.randomUUID().toString(),
                createdAt = null
            )
        )
        val chatBotGetRes2 = chatBotService.get(studentId = testStudentId)

        //then
        assertEquals(chatBotGetRes1.statusCode, HttpStatus.OK)
        assertEquals(chatBotGetRes2.statusCode, HttpStatus.OK)

        assertEquals(chatBotGetRes1.body!!.data!!.size, randomCount)
        assertEquals(chatBotGetRes2.body!!.data!!.size, randomCount + 1)
    }

    @Test
    fun testValidQuestionAndAnswer(){
        //given
        val chatBotChatList = mutableListOf<ChatBotChat>()
        val randomCount = (10..20).random()
        for (i in 1..randomCount) {
            chatBotChatList.add(ChatBotChat(
                id = null,
                studentId = testStudentId,
                question = UUID.randomUUID().toString(),
                answer = UUID.randomUUID().toString(),
                createdAt = null
            ))
        }
        chatBotChatRepository.saveAll(chatBotChatList)

        //when
        val chatBotGetRes1 = chatBotService.get(studentId = testStudentId)
        val chatBotChat = chatBotChatRepository.save(
            ChatBotChat(
                id = null,
                studentId = testStudentId,
                question = UUID.randomUUID().toString(),
                answer = UUID.randomUUID().toString(),
                createdAt = null
            )
        )
        val chatBotGetRes2 = chatBotService.get(studentId = testStudentId)

        //then
        assertEquals(chatBotGetRes1.statusCode, HttpStatus.OK)
        assertEquals(chatBotGetRes2.statusCode, HttpStatus.OK)

        for(i in 0..<randomCount) {
            assertEquals(chatBotGetRes1.body!!.data!![i], chatBotGetRes2.body!!.data!![i])
            assertEquals(chatBotGetRes1.body!!.data!![i].question, chatBotChatList[i].question)
            assertEquals(chatBotGetRes1.body!!.data!![i].answer, chatBotChatList[i].answer)
        }

        assertEquals(chatBotGetRes2.body!!.data!!.last().question, chatBotChat.question)
        assertEquals(chatBotGetRes2.body!!.data!!.last().answer, chatBotChat.answer)
    }

    @Test
    fun testValidLocalDateTime(){
        //given
        val chatBotChat = ChatBotChat(
            id = null,
            studentId = testStudentId,
            question = UUID.randomUUID().toString(),
            answer = UUID.randomUUID().toString(),
            createdAt = null
        )
        var now = LocalDateTime.now()
        val startTime = LocalDateTime.of(now.year, now.month, now.dayOfMonth, now.hour, now.minute, now.second)
        chatBotChatRepository.save(chatBotChat)

        //when
        val chatBotGetRes = chatBotService.get(studentId = testStudentId)
        now = LocalDateTime.now()
        val endTime = LocalDateTime.of(now.year, now.month, now.dayOfMonth, now.hour, now.minute, now.second)


        //then
        assertEquals(chatBotGetRes.statusCode, HttpStatus.OK)

        val createdAt = chatBotGetRes.body!!.data!![0].createdAt
        assertTrue(startTime.isBefore(createdAt) || startTime.isEqual(createdAt))
        assertTrue(endTime.isAfter(createdAt) || endTime.isEqual(createdAt))

    }

    @Test
    fun testInvalidStudentId(){
        //given
        val inValidTestStudentId = "INVALID_STUDENT_ID"
        chatBotChatRepository.save(
            ChatBotChat(
                id = null,
                studentId = testStudentId,
                question = UUID.randomUUID().toString(),
                answer = UUID.randomUUID().toString(),
                createdAt = null
            )
        )

        //when
        val chatBotGetRes = chatBotService.get(studentId = inValidTestStudentId)

        //then
        assertEquals(chatBotGetRes.statusCode, HttpStatus.OK)
        assertEquals(chatBotGetRes.body!!.data!!.size, 0)
    }

}