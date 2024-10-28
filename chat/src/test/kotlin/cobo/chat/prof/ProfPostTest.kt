package cobo.chat.prof

import cobo.chat.data.dto.prof.ProfPostReq
import cobo.chat.data.entity.Chat
import cobo.chat.data.entity.ChatRoom
import cobo.chat.data.enums.ChatStateEnum
import cobo.chat.repository.ChatRepository
import cobo.chat.repository.ChatRoomRepository
import cobo.chat.service.ChatService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
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

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProfPostTest @Autowired constructor(
    private val chatRepository: ChatRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val chatService: ChatService
){

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
    fun postProfTest(){
        val securityContextHolder = SecurityContextHolder.getContext()
        securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
            studentId, null, listOf(
                SimpleGrantedAuthority("PROFESSOR")
            ))
        val comment = UUID.randomUUID().toString()
        val countBefore = chatRepository.countByChatRoom(ChatRoom(studentId))

        //when
        val studentGetRes = chatService.profPost(ProfPostReq(studentId = studentId, comment = comment))
        val countAfter = chatRepository.countByChatRoom(ChatRoom(studentId))

        //then
        assert(countBefore == 0L)

        assert(studentGetRes.statusCode == HttpStatus.OK)

        assertEquals(countAfter, countBefore + 1)
    }

    @Test
    fun getChatState(){
        //given
        var now = LocalDateTime.now()
        val startTime = LocalDateTime.of(now.year, now.month, now.dayOfMonth, now.hour, now.minute, now.second)
        val securityContextHolder = SecurityContextHolder.getContext()
        securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
            studentId, null, listOf(
                SimpleGrantedAuthority("PROFESSOR")
            ))
        val comment = UUID.randomUUID().toString()

        //when
        val studentGetRes = chatService.profPost(ProfPostReq(studentId = studentId, comment = comment))
        now = LocalDateTime.now()
        val endTime = LocalDateTime.of(now.year, now.month, now.dayOfMonth, now.hour, now.minute, now.second)

        //then

        assert(studentGetRes.statusCode == HttpStatus.OK)

        val chat = chatRepository.findByChatRoom(chatRoom).last()
        val newChatRoom = chatRoomRepository.findById(chatRoom.id!!).orElseThrow()

        assert((chat.createdAt!!.isAfter(startTime) || chat.createdAt!!.isEqual(startTime))&& (chat.createdAt!!.isBefore(endTime) || chat.createdAt!!.isEqual(endTime)))
        assertFalse(chat.isQuestion)
        assertEquals(chat.comment, comment)
        assertEquals(chat.chatRoom.id, chatRoom.id)
        assertEquals(newChatRoom.chatStateEnum, ChatStateEnum.COMPLETE)
    }

    @Test
    fun postChatInMidPoint(){
        //given
        val testComment = UUID.randomUUID().toString()
        val securityContextHolder = SecurityContextHolder.getContext()
        securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
            studentId, null, listOf(
                SimpleGrantedAuthority("PROFESSOR")
            ))

        val startRandomCount = (5..10).random()
        val startChatList = mutableListOf<Chat>()

        for(i in 1..startRandomCount) {
            startChatList.add(
                Chat(
                id = null,
                chatRoom = chatRoom,
                comment = i.toString(),
                isQuestion = true,
                createdAt = null
            )
            )
        }

        val endRandomCount = (5..10).random()
        val endChatList = mutableListOf<Chat>()

        for(i in 1..endRandomCount) {
            endChatList.add(
                Chat(
                id = null,
                chatRoom = chatRoom,
                comment = i.toString(),
                isQuestion = true,
                createdAt = null
            )
            )
        }
        chatRepository.saveAll(startChatList)

        //when
        val postStudent = chatService.profPost(ProfPostReq(studentId, testComment))
        chatRepository.saveAll(endChatList)

        //then
        assertEquals(postStudent.statusCode, HttpStatus.OK)
        assertEquals(startRandomCount + 1 + endRandomCount, chatRepository.countByChatRoom(chatRoom).toInt())

        val chat = chatRepository.findByChatRoom(chatRoom)[startRandomCount]

        assertNotNull(chat)

        assertEquals(chat.chatRoom.chatStateEnum, ChatStateEnum.COMPLETE)
        assertEquals(chat.comment, testComment)
        assertFalse(chat.isQuestion)

    }
}