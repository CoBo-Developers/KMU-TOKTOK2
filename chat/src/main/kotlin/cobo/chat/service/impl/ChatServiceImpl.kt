package cobo.chat.service.impl

import cobo.chat.config.response.CoBoResponse
import cobo.chat.config.response.CoBoResponseDto
import cobo.chat.config.response.CoBoResponseStatus
import cobo.chat.data.dto.prof.ProfPostReq
import cobo.chat.data.dto.student.StudentGetElementRes
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

        this.post(
            studentId = studentId,
            comment = studentPostReq.question,
            isQuestion = true,
            chatStateEnum = ChatStateEnum.WAITING
        )

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun studentGet(authentication: Authentication): ResponseEntity<CoBoResponseDto<List<StudentGetElementRes>>> {

        val chatRoom = ChatRoom(id = authentication.name)

        return CoBoResponse(chatRepository.findByChatRoomWithJDBC(chatRoom).map{
            StudentGetElementRes(
                comment = it.comment,
                localDateTime = it.createdAt ?: LocalDateTime.now(),
                isQuestion = it.isQuestion
            )
        },CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun profPost(profPostReq: ProfPostReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {

        this.post(
            studentId = profPostReq.studentId,
            comment = profPostReq.comment,
            isQuestion = false,
            chatStateEnum = ChatStateEnum.COMPLETE
        )

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    private fun post(studentId: String, comment: String, isQuestion: Boolean, chatStateEnum: ChatStateEnum){

        val chatRoom = ChatRoom(id = studentId, chatStateEnum = chatStateEnum)

        val chat = Chat(
            chatRoom = chatRoom,
            comment = comment,
            isQuestion = isQuestion
        )

        if(isQuestion)
            chatRoomRepository.ifExistUpdateElseInsert(chatRoom)
        else{
            if(chatRoomRepository.update(chatRoom) <= 0)
                throw NullPointerException("Can't insert a new chat room")
        }

        chatRepository.insert(chat)
    }
}