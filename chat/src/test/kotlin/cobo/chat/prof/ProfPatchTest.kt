package cobo.chat.prof

import cobo.chat.data.entity.ChatRoom
import cobo.chat.data.enums.ChatStateEnum
import cobo.chat.repository.ChatRoomRepository
import cobo.chat.service.ChatService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import kotlin.test.assertTrue

@SpringBootTest
class ProfPatchTest @Autowired constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatService: ChatService,
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
        println("HELLO")
        println(chatRoomRepository.save(chatRoom))
    }

    @AfterEach
    fun clear(){
        chatRoomRepository.delete(chatRoom)
    }


    @Test
    fun testProfChatPatchFromWaiting(){
        this.testProfChatPatch(chatStateEnum = ChatStateEnum.WAITING)
    }

    @Test
    fun testProfChatPatchFromConfirmation(){
        this.testProfChatPatch(chatStateEnum = ChatStateEnum.CONFIRMATION)
    }


    @Test
    fun testProfChatPatchFromComplete(){
        this.testProfChatPatch(chatStateEnum = ChatStateEnum.COMPLETE)
    }

    private fun testProfChatPatch(chatStateEnum: ChatStateEnum){
        //given
        chatRoom.chatStateEnum = chatStateEnum
        chatRoomRepository.save(chatRoom)

        //when
        val profPatchRes = chatService.profPatch(studentId)

        //then
        assertEquals(HttpStatus.OK, profPatchRes.statusCode)

        val savedChatRoom = chatRoomRepository.findById(chatRoom.id!!)
        val expectedChatRoom = ChatRoom(id = studentId, chatStateEnum = ChatStateEnum.COMPLETE)

        assertTrue(savedChatRoom.isPresent)

        assertEquals(expectedChatRoom, savedChatRoom.get())

    }
}