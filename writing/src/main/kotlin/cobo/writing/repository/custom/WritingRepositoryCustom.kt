package cobo.writing.repository.custom

import cobo.writing.data.entity.Writing

interface WritingRepositoryCustom {

    fun ifExistsUpdateElseInsert(writing: Writing): Int
    fun updateStateByAssignmentIdAndStudentIdWithJDBC(writingState: Short, assignmentId: Int, studentId: String): Int
    fun findByAssignmentIdOrderByStatePagingWithJDBC(assignmentId: Int, page: Int, pageSize: Int): List<Writing>
    fun countByAssignmentIdWithJDBC(assignmentId: Int): Long
}