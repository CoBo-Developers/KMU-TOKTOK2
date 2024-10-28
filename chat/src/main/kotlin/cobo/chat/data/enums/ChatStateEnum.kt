package cobo.chat.data.enums

enum class ChatStateEnum(val value: Short) {

    WAITING(0),
    CONFIRMATION(1),
    COMPLETE(2);

    companion object {
        fun from(findValue: Short) = entries.find { it.value == findValue }
    }
}