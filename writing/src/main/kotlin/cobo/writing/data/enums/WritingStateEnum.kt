package cobo.writing.data.enums

enum class WritingStateEnum(val value: Short) {

    NOT_SUBMITTED(0),
    SUBMITTED(1),
    NOT_APPROVED(2),
    APPROVED(3);

    companion object {
        fun from(findValue: Short) = entries.find { it.value == findValue }
    }
}