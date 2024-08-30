package cobo.auth.repository

import cobo.auth.data.entity.StudentInfo
import org.springframework.data.jpa.repository.JpaRepository

interface StudentInfoRepository: JpaRepository<StudentInfo, Int> {
    fun existsByStudentIdAndName(studentId: String, name: String): Boolean
}