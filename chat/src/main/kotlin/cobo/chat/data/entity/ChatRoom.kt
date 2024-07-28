package cobo.chat.data.entity

import cobo.chat.data.enum.ChatStateEnum
import jakarta.persistence.*

@Entity
data class ChatRoom(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Enumerated(EnumType.ORDINAL)
    val chatStateEnum: ChatStateEnum
)
