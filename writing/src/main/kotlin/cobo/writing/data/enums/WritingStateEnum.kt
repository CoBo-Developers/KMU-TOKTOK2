package cobo.writing.data.enums

enum class WritingStateEnum(val value: Short) {

    NOT_SUBMITTED(1),
    SUBMITTED(2),
    NOT_APPROVED(3),
    APPROVED(4);

    companion object {
        fun from(findValue: Short) = entries.find { it.value == findValue }
    }
}