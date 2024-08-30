package cobo.auth.data.entity

import jakarta.persistence.*

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "student_id_and_name", columnNames = ["student_id", "name"])
    ]
)
data class StudentInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @Column(length = 30)
    var studentId:String,

    @Column(length = 30)
    var name: String
)
