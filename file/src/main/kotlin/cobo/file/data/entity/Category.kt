package cobo.file.data.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE assignment SET deleted = true where id = ?")
data class Category(
    @Id
    var name: String,
    var deleted: Boolean

)
