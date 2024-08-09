package cobo.writing.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate

@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE assignment SET deleted = true where id = ?")
data class Assignment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,

    @Column(length = 200)
    var title: String?,

    @Column(length = 500)
    var description: String?,

    var score: Int?,

    var startDate: LocalDate?,

    var endDate: LocalDate?,

    var deleted: Boolean = false,
){
    constructor(id: Int?): this(
        id = id, title = null, description = null, score = null, startDate = null, endDate = null
    )
}