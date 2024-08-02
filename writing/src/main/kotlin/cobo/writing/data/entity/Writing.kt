package cobo.writing.data.entity

import cobo.writing.data.enums.WritingStateEnum
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    indexes = [
        Index(name = "student_id_index", columnList = "studentId"),
    ]
)
data class Writing(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,

    val studentId: String,

    val week: Short,

    @Column(length = 2000)
    val content: String,

    val state: WritingStateEnum,

    val createdAt: LocalDateTime?,

    val updatedAt: LocalDateTime?,

    val submittedAt: LocalDateTime?
    )
