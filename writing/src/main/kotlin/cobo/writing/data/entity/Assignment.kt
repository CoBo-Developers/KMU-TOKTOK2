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
    var id: Int?,

    @Column(length = 200)
    var title: String?,

    @Column(length = 500)
    var description: String?,

    @Column(length = 400)
    var prompt: String?,

    var score: Int?,

    var startDate: LocalDate?,

    var endDate: LocalDate?,

    var deleted: Boolean = false,
){
    constructor(id: Int?): this(
        id = id, title = null, description = null, prompt = null, score = null, startDate = null, endDate = null
    )
}