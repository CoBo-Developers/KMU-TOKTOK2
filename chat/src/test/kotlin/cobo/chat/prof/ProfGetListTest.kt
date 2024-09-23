package cobo.chat.prof

import cobo.chat.data.entity.Chat
import cobo.chat.data.entity.ChatRoom
import cobo.chat.data.enums.ChatStateEnum
import cobo.chat.repository.ChatRepository
import cobo.chat.repository.ChatRoomRepository
import cobo.chat.service.ChatService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random
import kotlin.test.assertEquals

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProfGetListTest @Autowired constructor(
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
    fun testGet(){
        //given
        val count = chatRoomRepository.count()
        val pageSize = 10

        //when
        val profGetListRes = chatService.profGetList(0, pageSize)

        //then
        assertEquals(profGetListRes.statusCode, HttpStatus.OK)
        assertEquals(profGetListRes.body!!.data!!.totalElement, count)
    }

    @Test
    fun testWaitingState(){
        testByState(ChatStateEnum.WAITING)
    }

    @Test
    fun testConfirmation(){
        testByState(ChatStateEnum.CONFIRMATION)
    }

    @Test
    fun testComplete(){
        testByState(ChatStateEnum.COMPLETE)
    }

    fun testByState(chatRoomStateEnum: ChatStateEnum){
        //given
        var now = LocalDateTime.now()
        val startTime = LocalDateTime.of(now.year, now.month, now.dayOfMonth, now.hour, now.minute, now.second)
        val testComment = UUID.randomUUID().toString()
        val testIsQuestion = Random.nextBoolean()
        var chat = chatRepository.save(Chat(
            id = null,
            chatRoom = chatRoom,
            comment = testComment,
            isQuestion = testIsQuestion,
            createdAt = null
        ))
        chatRoom.chatStateEnum = chatRoomStateEnum
        chatRoomRepository.save(chatRoom)

        //when
        var previousCount = 0L
        for(i in 0..<chatRoomStateEnum.value){
            previousCount += chatRoomRepository.countByChatStateEnum(ChatStateEnum.from(i.toShort())!!)
        }
        val profGetListRes = chatService.profGetList(previousCount.toInt(), 1)
        now = LocalDateTime.now()
        val endTime = LocalDateTime.of(now.year, now.month, now.dayOfMonth, now.hour, now.minute, now.second)

        //then
        val profGetListElement = profGetListRes.body!!.data!!.chatList[0]

        chat = chatRepository.findById(chat.id!!).orElseThrow()

        assertEquals(profGetListRes.statusCode, HttpStatus.OK)
        assertEquals(profGetListElement.chatState, chatRoomStateEnum)
        assertEquals(profGetListElement.comment, testComment)
        assertEquals(profGetListElement.studentId, studentId)
        assertTrue(chat.createdAt!!.isAfter(startTime) || chat.createdAt!!.isEqual(startTime))

//        val tolerance = 10000L
//        val startMillis = startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
//        val endMillis = endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
//        val chatCreatedMillis = chat.createdAt!!.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
//
//        assertTrue(chatCreatedMillis in startMillis - tolerance..endMillis + tolerance)

    }

    @Test
    fun testGetAll(){
        //given
        val count = chatRoomRepository.count()
        chatRepository.save(Chat(
            id = null,
            chatRoom = chatRoom,
            comment = "",
            isQuestion = false,
            createdAt = null
        ))
        val chatRoomList = chatRoomRepository.findAllByOrderByChatStateEnum()

        //when
        val profGetListRes = chatService.profGetList(0, count.toInt())

        //then
        assertEquals(profGetListRes.statusCode, HttpStatus.OK)
        assertEquals(profGetListRes.body!!.data!!.totalElement, count)
        assertEquals(count.toInt(), profGetListRes.body!!.data!!.chatList.size)

        var i = 0

        val hastSetOfStudentId = hashSetOf<String>()

        profGetListRes.body!!.data!!.chatList.forEach {
            if (hastSetOfStudentId.contains(it.studentId)){
                assert(false)
            }
            else{
                hastSetOfStudentId.add(it.studentId)
                assertEquals(it.chatState, chatRoomList[i].chatStateEnum)
            }
            i++
        }
    }
}