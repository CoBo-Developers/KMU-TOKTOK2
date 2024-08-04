package cobo.chat.prof

import cobo.chat.data.entity.Chat
import cobo.chat.data.entity.ChatRoom
import cobo.chat.data.enums.ChatStateEnum
import cobo.chat.repository.ChatRepository
import cobo.chat.repository.ChatRoomRepository
import cobo.chat.service.ChatService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertNotNull

@SpringBootTest
class ProfGetTest @Autowired constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRepository: ChatRepository,
    private val chatService: ChatService,
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

        //when
        val profGetRes = chatService.profGet(studentId)

        //then
        assert(profGetRes.statusCode == HttpStatus.OK)
        assert(profGetRes.body?.data?.size == 0)
    }

    @Test
    fun getQuestion(){
        getState(isQuestion = true)
    }

    @Test
    fun getNotQuestion(){
        getState(isQuestion = false)
    }

    @Test
    fun testChangedChatRoomState(){
        //given
        chatRepository.save(Chat(
            id = null,
            chatRoom = chatRoom,
            comment = "",
            isQuestion = true,
            createdAt = null))

        //when
        val profGetRes = chatService.profGet(studentId)

        //then
        assert(profGetRes.statusCode == HttpStatus.OK)
        val chatRoom = chatRoomRepository.findById(studentId).orElseThrow()
        assert(chatRoom.chatStateEnum == ChatStateEnum.CONFIRMATION)
    }


    fun getState(isQuestion: Boolean){
        //given
        val testComment = UUID.randomUUID().toString()
        chatRepository.save(Chat(
            id = null,
            chatRoom = chatRoom,
            comment = testComment,
            isQuestion = isQuestion,
            createdAt = null))

        //when
        val profGetRes = chatService.profGet(studentId)

        //then
        assert(profGetRes.statusCode == HttpStatus.OK)
        assert(profGetRes.body?.data?.size == 1)
        assert(profGetRes.body!!.data!![0].isQuestion == isQuestion)
        assert(profGetRes.body!!.data!![0].comment == testComment)
        assert(profGetRes.body!!.data!![0].localDateTime.isBefore(LocalDateTime.now()))
    }

    @Test
    fun getNotValidUser(){
        //given
        val notValidStudentId = "NOT_VALID"

        //when
        val profGetRes = chatService.profGet(notValidStudentId)

        //then
        assert(profGetRes.statusCode == HttpStatus.OK)
        assert(profGetRes.body?.data?.size == 0)

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

        //when
        val profGetRes = chatService.profGet(studentId)

        assert(profGetRes.statusCode == HttpStatus.OK)
        assert(profGetRes.body?.data?.size == randomCount + 1)

        //then
        for (i in 0..<randomCount) {
            val chat = profGetRes.body!!.data!![i]

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

        //when
        val profGetRes1 = chatService.profGet(studentId)
        chatRepository.save(chatList.last())
        val profGetRes2 = chatService.profGet(studentId)

        assert(profGetRes1.statusCode == HttpStatus.OK)
        assert(profGetRes1.statusCode == HttpStatus.OK)
        assert(profGetRes2.body?.data?.size ==  profGetRes1.body?.data?.size!! + 1)

        //then
        for (i in 0..<randomCount - 1) {
            val chat = profGetRes1.body!!.data!![i]

            assert(chat.isQuestion == chatList[i].isQuestion)
            assert(chat.comment == chatList[i].comment)

            assertNotNull(chat.localDateTime)
        }

        assert(profGetRes2.body?.data!!.last().isQuestion == chatList.last().isQuestion)
        assert(profGetRes2.body?.data!!.last().comment == chatList.last().comment)

        assertNotNull(profGetRes2.body!!.data!!.last().localDateTime)
    }
}