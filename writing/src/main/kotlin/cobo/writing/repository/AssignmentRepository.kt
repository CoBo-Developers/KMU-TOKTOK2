package cobo.writing.repository

import cobo.writing.data.entity.Assignment
import cobo.writing.repository.custom.AssignmentRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AssignmentRepository : JpaRepository<Assignment, Int>, AssignmentRepositoryCustom {

    fun findTopByOrderByIdDesc(): Optional<Assignment>
}