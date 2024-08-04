package cobo.writing.data.dto.assignment

import java.time.LocalDate

data class AssignmentPostReq(
    val title: String,

    val description: String,

    val score: Int,

    val startDate: LocalDate,

    val endDate: LocalDate,
)