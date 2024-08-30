package cobo.auth.repository

import cobo.auth.data.entity.StudentInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface StudentInfoRepository: JpaRepository<StudentInfo, Int> {
    fun existsByStudentIdAndName(studentId: String, name: String): Boolean
    @Transactional
    fun deleteByStudentIdAndName(studentId: String, name: String)
}