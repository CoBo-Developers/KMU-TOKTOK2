package cobo.writing.data.dto.student

data class StudentGetListRes(
    val assignmentList: List<StudentGetListResElement>
)

data class StudentGetListResElement(
    val name: String
)