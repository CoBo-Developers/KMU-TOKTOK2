package cobo.chat.data.enums

enum class RoleEnum(val value: Short) {
    DEVELOPER(0),
    PROFESSOR(1),
    STUDENT(2);

    companion object {
        fun from(findValue: Short) = entries.first { it.value == findValue }
    }
}