package cobo.auth.config.response

enum class CoBoResponseStatus(
    val code: Int,
    val message: String
) {

    SUCCESS(2000, "OK"),
    NO_DATA_CHANGES(2001, "SUCCESS, BUT NO DATA CHANGES"),


    BAD_REQUEST(4000, "BAD REQUEST"),
}