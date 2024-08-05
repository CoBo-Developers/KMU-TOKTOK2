package cobo.writing.config.response

enum class CoBoResponseStatus(
    val code: Int,
    val message: String
) {

    SUCCESS(2000, "OK"),
    NO_DATA_CHANGES(2001, "SUCCESS, BUT NO DATA CHANGES"),
    CREATED(2020, "CREATED_SUCCESS"),


    BAD_REQUEST(4000, "BAD REQUEST"),
    INVALID_RANGE(4001, "INVALID RANGE"),
    NOT_AUTHORIZATION(4011, "AUTHORIZATION TOKEN IS EMPTY"),
    NOT_FOUND_USER(4041, "USER NOT FOUND"),
}