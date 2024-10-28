package cobo.writing.repository

import cobo.writing.data.entity.Feedback
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackRepository: JpaRepository<Feedback, Int> {
}