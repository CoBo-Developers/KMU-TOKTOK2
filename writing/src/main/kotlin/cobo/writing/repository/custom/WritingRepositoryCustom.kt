package cobo.writing.repository.custom

import cobo.writing.data.entity.Assignment
import cobo.writing.data.entity.Writing
import java.util.*

interface WritingRepositoryCustom {

    fun ifExistsUpdateElseInsert(writing: Writing): Int
    fun updateStateAndScoreByAssignmentIdAndStudentIdWithJDBC(
        writingState: Short,
        score: Int,
        assignmentId: Int,
        studentId: String): Int
    fun findByAssignmentIdOrderByStatePagingWithJDBC(assignmentId: Int, page: Int, pageSize: Int): List<Writing>
    fun countByAssignmentIdWithJDBC(assignmentId: Int): Long
    fun findByAssignmentAndStudentIdWithJDBC(assignment: Assignment, studentId: String): Optional<Writing>
    fun findByStudentIdWithJDBC(studentId: String?): List<Writing>
}