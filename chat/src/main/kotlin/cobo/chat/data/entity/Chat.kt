package cobo.chat.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
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
    val id: Int,

    @ManyToOne
    val chatRoom: ChatRoom,

    @Column(length = 10000)
    val comment: String,

    val isQuestion: Boolean,

    @CreationTimestamp
    val createdAt: LocalDateTime?
)
