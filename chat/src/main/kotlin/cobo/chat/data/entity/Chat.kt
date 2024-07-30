package cobo.chat.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@Table(
    indexes = [
        Index(name = "idx_chat_room", columnList = "chat_room_id")
    ]
)
@DynamicInsert
data class Chat(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,

    @ManyToOne
    val chatRoom: ChatRoom,

    @Column(length = 10000)
    val comment: String,

    val isQuestion: Boolean,

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdAt: LocalDateTime?
){
    constructor(
        chatRoom: ChatRoom,
        comment: String,
        isQuestion: Boolean) : this(
        id = null,
        chatRoom = chatRoom,
        comment = comment,
        isQuestion = isQuestion,
        createdAt = null)
}