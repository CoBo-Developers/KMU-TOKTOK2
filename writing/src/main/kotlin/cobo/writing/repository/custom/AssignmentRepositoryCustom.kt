package cobo.writing.repository.custom

import cobo.writing.data.dto.student.StudentGetListResElement

interface AssignmentRepositoryCustom {
    fun findByUserWithJDBC(studentId: String?): List<StudentGetListResElement>
}