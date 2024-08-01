package cobo.chat.chatBot

import cobo.chat.config.LogFilter
import cobo.chat.data.dto.chatBot.ChatBotPostReq
import cobo.chat.data.enum.RoleEnum
import cobo.chat.repository.ChatBotChatRepository
import cobo.chat.service.ChatBotService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ChatBotPostTest @Autowired constructor(
    private val chatBotChatRepository: ChatBotChatRepository,
    private val chatBotService: ChatBotService
){

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
    fun testValidAnswer(){
        //given
        val question = UUID.randomUUID().toString()
        val securityContextHolder = SecurityContextHolder.getContext()
        securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
            testStudentId, null, listOf(
                SimpleGrantedAuthority(RoleEnum.STUDENT.name)
            ))


        //when
        val chatBotPostRes = chatBotService.post(ChatBotPostReq(question = question), securityContextHolder.authentication)


        //then
        assertEquals(chatBotPostRes.statusCode, HttpStatus.OK)
        assertNotEquals(chatBotPostRes.body!!.data!!.answer, question)
    }

    @Test
    fun testValidTime(){
        //given
        var now = LocalDateTime.now()
        val startTime = LocalDateTime.of(now.year, now.month, now.dayOfMonth, now.hour, now.minute, now.second)
        val question = UUID.randomUUID().toString()
        val securityContextHolder = SecurityContextHolder.getContext()
        securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
            testStudentId, null, listOf(
                SimpleGrantedAuthority(RoleEnum.STUDENT.name)
            ))

        //when
        chatBotService.post(ChatBotPostReq(question = question), securityContextHolder.authentication)
        now = LocalDateTime.now()
        val endTime = LocalDateTime.of(now.year, now.month, now.dayOfMonth, now.hour, now.minute, now.second)

        //then
        val chatBotChat = chatBotChatRepository.findByStudentIdWithJDBC(studentId = testStudentId).last()

        assertTrue{
            chatBotChat.createdAt!!.isAfter(startTime) || chatBotChat.createdAt!!.isEqual(startTime)
            chatBotChat.createdAt!!.isBefore(endTime) || chatBotChat.createdAt!!.isEqual(endTime)
        }
    }
}