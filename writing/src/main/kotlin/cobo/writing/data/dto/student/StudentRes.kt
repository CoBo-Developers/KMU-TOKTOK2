package cobo.writing.data.dto.student

import java.time.LocalDate

data class StudentGetListRes(
    val assignmentList: List<StudentGetListResElement>
)

data class StudentGetListResElement(
    val id: Int,

    val title: String,

    val description: String,

    val score: Int,

    val startDate: LocalDate,

    val endDate: LocalDate,

    var writingState: Short,

    var writingScore: Int
)

data class StudentGetRes(

    val assignmentId: Int,

    val content: String
)

data class StudentPostFeedbackRes(
    val feedback: String
)