package cobo.writing.repository

import cobo.writing.data.entity.Assignment
import cobo.writing.repository.custom.AssignmentRepositoryCustom
import java.util.*

interface AssignmentRepository: AssignmentRepositoryCustom {

    fun findTopByOrderByIdDesc(): Optional<Assignment>
    fun findById(id: Int): Optional<Assignment>
    fun save(assignment: Assignment): Assignment
    fun findAll():List<Assignment>
    fun delete(assignment: Assignment)
    fun deleteAll(assignmentList: MutableList<Assignment>)
    fun count(): Long
    fun deleteAll()
    fun saveAll(assignmentList: List<Assignment>): List<Assignment>
}