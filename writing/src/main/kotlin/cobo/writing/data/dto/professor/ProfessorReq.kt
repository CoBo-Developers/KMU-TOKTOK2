package cobo.writing.data.dto.professor

import java.time.LocalDate

data class AssignmentPostReq(
    val title: String,

    val description: String,

    val score: Int,

    val startDate: LocalDate,

    val endDate: LocalDate,
)

data class AssignmentPutReq(
    val id: Int,

    val title: String,

    val description: String,

    val score: Int,

    val startDate: LocalDate,

    val endDate: LocalDate,
)