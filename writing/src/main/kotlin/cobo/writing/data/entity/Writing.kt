package cobo.writing.data.entity

import cobo.writing.data.enums.WritingStateEnum
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    indexes = [
        Index(name = "student_id_index", columnList = "student_id"),
    ],
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["student_id", "assignment_id"])
    ]
)
data class Writing(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,

    val studentId: String,

    @ManyToOne
    val assignment: Assignment,

    @Column(length = 2000)
    val content: String,

    val state: WritingStateEnum,

    var score: Int,

    val createdAt: LocalDateTime?,

    val updatedAt: LocalDateTime?,

    val submittedAt: LocalDateTime?
    )
