package cobo.writing.repository

import cobo.writing.data.entity.Assignment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AssignmentRepository : JpaRepository<Assignment, Int> {

    fun findTopByOrderByIdDesc(): Optional<Assignment>
}