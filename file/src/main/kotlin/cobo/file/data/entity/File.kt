package cobo.file.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE file SET deleted = true WHERE id = ?")
data class File(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    var name: String,
    var fileName: String,
    @ManyToOne
    var category: Category,
    var path: String,
    var size: Long,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var deleted: Boolean
)
