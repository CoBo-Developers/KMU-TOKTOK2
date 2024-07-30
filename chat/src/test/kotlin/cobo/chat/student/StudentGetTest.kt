package cobo.chat.student

import cobo.chat.data.entity.Chat
import cobo.chat.data.entity.ChatRoom
import cobo.chat.data.enum.ChatStateEnum
import cobo.chat.repository.ChatRepository
import cobo.chat.repository.ChatRoomRepository
import cobo.chat.service.ChatService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertNotNull

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentGetTest @Autowired constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRepository: ChatRepository,
    private val chatService: ChatService
) {

    private val studentId = "test"

    private val chatRoom = ChatRoom(
        id = studentId,
        chatStateEnum = ChatStateEnum.WAITING
    )

    @BeforeEach
    fun init(){
        chatRoom.id = studentId
        chatRoom.chatStateEnum = ChatStateEnum.WAITING
        chatRoomRepository.save(chatRoom)
    }

    @AfterEach
    fun clear(){
        chatRepository.deleteByChatRoom(chatRoom)
        chatRoomRepository.delete(chatRoom)
    }

    @Test
    fun getEmpty(){
        //given
        val securityContextHolder = SecurityContextHolder.getContext()
        securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
            studentId, null, listOf(
                SimpleGrantedAuthority("USER")
            ))

        //when
        val studentGetRes = chatService.studentGet(securityContextHolder.authentication)

        //then
        assert(studentGetRes.statusCode == HttpStatus.OK)
        assert(studentGetRes.body?.data?.size == 0)
    }

    @Test
    fun getQuestion(){
        getState(isQuestion = true)
    }

    @Test
    fun getNotQuestion(){
        getState(isQuestion = false)
    }

    fun getState(isQuestion: Boolean){
        //given
        val testComment = UUID.randomUUID().toString()
        val securityContextHolder = SecurityContextHolder.getContext()
        securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
            studentId, null, listOf(
                SimpleGrantedAuthority("USER")
            ))

        chatRepository.save(Chat(
            id = null,
            chatRoom = chatRoom,
            comment = testComment,
            isQuestion = isQuestion,
            createdAt = null))

        //when
        val studentGetRes = chatService.studentGet(securityContextHolder.authentication)

        //then
        assert(studentGetRes.statusCode == HttpStatus.OK)
        assert(studentGetRes.body?.data?.size == 1)
        assert(studentGetRes.body!!.data!![0].isQuestion == isQuestion)
        assert(studentGetRes.body!!.data!![0].comment == testComment)
        assert(studentGetRes.body!!.data!![0].localDateTime.isBefore(LocalDateTime.now()))
    }

    @Test
    fun getNotValidUser(){
        //given
        val securityContextHolder = SecurityContextHolder.getContext()
        securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
            "NOT_VALID", null, listOf(
                SimpleGrantedAuthority("USER")
            ))

        //when
        val studentGetRes = chatService.studentGet(securityContextHolder.authentication)
        assert(studentGetRes.statusCode == HttpStatus.OK)
        assert(studentGetRes.body?.data?.size == 0)

    }

    @Test
    fun getRandomChatCount(){
        //given
        val chatList = mutableListOf<Chat>()
        val randomCount = (15..20).random()
        for (i in 0..randomCount) {
            chatList.add(
                Chat(
                    id = null,
                    chatRoom = chatRoom,
                    comment = UUID.randomUUID().toString(),
                    isQuestion = kotlin.random.Random.nextBoolean(),
                    createdAt = null
                )
            )
        }
        chatRepository.saveAll(chatList)
        val securityContextHolder = SecurityContextHolder.getContext()
        securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
            studentId, null, listOf(
                SimpleGrantedAuthority("USER")
            ))

        //when
        val studentGetRes = chatService.studentGet(securityContextHolder.authentication)

        assert(studentGetRes.statusCode == HttpStatus.OK)
        assert(studentGetRes.body?.data?.size == randomCount + 1)

        //then
        for (i in 0..<randomCount) {
            val chat = studentGetRes.body!!.data!![i]

            assert(chat.isQuestion == chatList[i].isQuestion)
            assert(chat.comment == chatList[i].comment)
            assertNotNull(chat.localDateTime)
        }
    }

    @Test
    fun getAddChat(){
        //given
        val chatList = mutableListOf<Chat>()
        val randomCount = (15..20).random()
        for (i in 0..randomCount) {
            chatList.add(
                Chat(
                    id = null,
                    chatRoom = chatRoom,
                    comment = UUID.randomUUID().toString(),
                    isQuestion = kotlin.random.Random.nextBoolean(),
                    createdAt = null
                )
            )
        }
        chatRepository.saveAll(chatList.subList(0, randomCount - 1))
        val securityContextHolder = SecurityContextHolder.getContext()
        securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
            studentId, null, listOf(
                SimpleGrantedAuthority("USER")
            ))

        //when
        val studentGetRes1 = chatService.studentGet(securityContextHolder.authentication)
        chatRepository.save(chatList.last())
        val studentGetRes2 = chatService.studentGet(securityContextHolder.authentication)

        assert(studentGetRes1.statusCode == HttpStatus.OK)
        assert(studentGetRes1.statusCode == HttpStatus.OK)
        assert(studentGetRes2.body?.data?.size ==  studentGetRes1.body?.data?.size!! + 1)

        //then
        for (i in 0..<randomCount - 1) {
            val chat = studentGetRes1.body!!.data!![i]

            assert(chat.isQuestion == chatList[i].isQuestion)
            assert(chat.comment == chatList[i].comment)

            assertNotNull(chat.localDateTime)
        }

        assert(studentGetRes2.body?.data!!.last().isQuestion == chatList.last().isQuestion)
        assert(studentGetRes2.body?.data!!.last().comment == chatList.last().comment)

        assertNotNull(studentGetRes2.body!!.data!!.last().localDateTime)
    }
}