package cobo.auth.data.enums

enum class OauthTypeEnum(val value: Short) {
    KAKAO(1),
    NAVER(2),
    GOOGLE(3);

    companion object {
        fun from(findValue: Short) = entries.find { it.value == findValue }
    }
}