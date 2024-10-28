package cobo.auth.data.enums

enum class RegisterStateEnum(val value: Short) {
    ACTIVE(0),
    INACTIVE(1);

    companion object {
        fun from(findValue: Short) = entries.find { it.value == findValue }
    }
}