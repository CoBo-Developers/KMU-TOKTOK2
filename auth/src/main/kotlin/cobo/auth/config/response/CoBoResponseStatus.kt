package cobo.auth.config.response

enum class CoBoResponseStatus(
    val code: Int,
    val message: String
) {

    SUCCESS(2000, "OK"),

    BAD_REQUEST(4000, "Bad Request"),
}