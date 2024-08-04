package cobo.writing.data.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class Assignment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,

    @Column(length = 200)
    var title: String?,

    @Column(length = 500)
    var description: String?,

    var score: Int,

    var startDate: LocalDate,

    var endDate: LocalDate
)
