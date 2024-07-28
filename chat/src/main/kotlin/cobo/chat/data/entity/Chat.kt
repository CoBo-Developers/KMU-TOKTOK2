package cobo.chat.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.DynamicInsert
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(
    indexes = [
        Index(name = "idx_chat_room", columnList = "chat_room_id")
    ]
)
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