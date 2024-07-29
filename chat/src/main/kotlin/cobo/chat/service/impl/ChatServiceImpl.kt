package cobo.chat.service.impl

import cobo.chat.config.response.CoBoResponse
import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.config.response.CoBoResponseStatus
import cobo.chat.data.dto.student.StudentGetElementRes
import cobo.chat.data.dto.student.StudentGetRes
import cobo.chat.data.dto.student.StudentPostReq
import cobo.chat.data.entity.Chat
import cobo.chat.data.entity.ChatRoom
import cobo.chat.data.enum.ChatStateEnum
import cobo.chat.repository.ChatRepository
import cobo.chat.repository.ChatRoomRepository
import cobo.chat.service.ChatService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatServiceImpl(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRepository: ChatRepository
): ChatService {
    override fun studentPost(
        studentPostReq: StudentPostReq,
        authentication: Authentication
    ): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {

        val studentId: String = authentication.name
        val chatRoom = ChatRoom(id = studentId, chatStateEnum = ChatStateEnum.WAITING)
        val chat = Chat(
            chatRoom = chatRoom,
            comment = studentPostReq.question,
            isQuestion = true
        )

        chatRoomRepository.ifExistUpdateElseInsert(chatRoom)
        chatRepository.insert(chat)

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun studentGet(authentication: Authentication): ResponseEntity<CoBoResponseDto<List<StudentGetElementRes>>> {

        val chatRoom = ChatRoom(id = authentication.name)

        return CoBoResponse(chatRepository.findByChatRoom(chatRoom).map{
            StudentGetElementRes(
                comment = it.comment,
                localDateTime = it.createdAt ?: LocalDateTime.now(),
                isQuestion = it.isQuestion
            )
        },CoBoResponseStatus.SUCCESS).getResponseEntity()
    }
}