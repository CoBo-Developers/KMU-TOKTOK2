package cobo.writing.data.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Feedback (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(length = 2000)
    val request: String,

    @Column(length = 2000)
    val response: String,

    @Column(length = 20)
    val studentId: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),
)