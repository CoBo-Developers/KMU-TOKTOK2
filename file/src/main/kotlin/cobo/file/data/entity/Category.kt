package cobo.file.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE assignment SET deleted = true where id = ?")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(unique = true)
    var name: String,

    var deleted: Boolean? = false

)
