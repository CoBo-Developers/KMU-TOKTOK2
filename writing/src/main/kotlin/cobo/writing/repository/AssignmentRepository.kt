package cobo.writing.repository

import cobo.writing.data.entity.Assignment
import org.springframework.data.jpa.repository.JpaRepository

interface AssignmentRepository : JpaRepository<Assignment, Int> {
}