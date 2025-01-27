package cobo.chat.config.response

enum class CoBoResponseStatus(
    val code: Int,
    val message: String
) {

    SUCCESS(2000, "OK"),
    NO_DATA_CHANGES(2001, "SUCCESS, BUT NO DATA CHANGES"),


    BAD_REQUEST(4000, "BAD REQUEST"),
    NOT_AUTHORIZATION(4011, "AUTHORIZATION TOKEN IS EMPTY"),
    NOT_FOUND_USER(4041, "USER NOT FOUND"),
    CANT_SAVE(4042, "CANT SAVE IN DATABASE"),
    NOT_FOUND_STUDENT(4043, "TARGET STUDENT NOT FOUND"),
    CANT_GET_RESOURCES(4044, "CANT GET RESOURCES"),
}