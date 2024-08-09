package cobo.writing.repository.custom

import cobo.writing.data.entity.Writing

interface WritingRepositoryCustom {

    fun ifExistsUpdateElseInsert(writing: Writing): Int
    fun updateStateByAssignmentIdAndStudentIdWithJDBC(writingState: Short, assignmentId: Int, studentId: String): Int
}