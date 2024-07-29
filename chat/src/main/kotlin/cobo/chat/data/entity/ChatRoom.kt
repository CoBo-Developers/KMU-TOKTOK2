package cobo.chat.data.entity

import cobo.chat.data.enum.ChatStateEnum
import jakarta.persistence.*

@Entity
data class ChatRoom(
    @Id
    @Column(length = 20, unique = true, nullable = false)
    var id: String?,

    @Enumerated(EnumType.ORDINAL)
    var chatStateEnum: ChatStateEnum
) {
    constructor(id: String) : this(
        id = id,
        chatStateEnum = ChatStateEnum.WAITING
    )
}
