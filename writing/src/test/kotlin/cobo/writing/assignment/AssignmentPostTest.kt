package cobo.writing.assignment

import cobo.writing.data.dto.assignment.AssignmentPostReq
import cobo.writing.data.entity.Assignment
import cobo.writing.repository.AssignmentRepository
import cobo.writing.service.AssignmentService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDate
import kotlin.test.assertEquals

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AssignmentPostTest @Autowired constructor(
    private val assignmentRepository: AssignmentRepository,
    private val assignmentService: AssignmentService
) {

    private val assignment = Assignment(
        id = null,
        title = "Test title",
        description = "Test description",
        score = 10,
        startDate = LocalDate.parse("2024-08-01"),
        endDate = LocalDate.parse("2024-08-08")
    )

    private val assignmentList = mutableListOf<Assignment>()

    @AfterEach
    fun clear(){
        assignmentRepository.delete(assignment)
        assignmentRepository.deleteAll(assignmentList)
        assignmentList.clear()
    }

    @Test
    fun testPost(){
        //given
        val assignmentPostReq = AssignmentPostReq(
            title = assignment.title ?: "",
            description = assignment.description ?: "",
            score = assignment.score,
            startDate = assignment.startDate,
            endDate = assignment.endDate,
        )

        //when
        assignmentService.post(assignmentPostReq)


        //then
        val optionalSavedAssignment = assignmentRepository.findTopByOrderByIdDesc()

        assertTrue(optionalSavedAssignment.isPresent)
        val savedAssignment = optionalSavedAssignment.get()

        assertEquals(assignment.title, savedAssignment.title)
        assertEquals(assignment.description, savedAssignment.description)
        assertEquals(assignment.score, savedAssignment.score)
        assertEquals(assignment.startDate, savedAssignment.startDate)
        assertEquals(assignment.endDate, savedAssignment.endDate)
    }

    @Test
    fun testPostInMiddle(){
        //given
        val startRandomCount = (5..10).random()
        val endRandomCount = (5..10).random()

        //when

        //then

    }
}