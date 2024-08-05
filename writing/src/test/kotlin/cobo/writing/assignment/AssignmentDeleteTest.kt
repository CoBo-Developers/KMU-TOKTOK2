package cobo.writing.assignment

import cobo.writing.data.entity.Assignment
import cobo.writing.repository.AssignmentRepository
import cobo.writing.service.AssignmentService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDate
import java.util.*

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AssignmentDeleteTest @Autowired constructor(
    private val assignmentRepository: AssignmentRepository,
    private val assignmentService: AssignmentService
){

    private val assignmentList = mutableListOf<Assignment>()

    @AfterEach
    fun clear(){
        assignmentRepository.deleteAll(assignmentList)
        assignmentList.clear()
    }

    private fun makeTestAssignment():Assignment{
        return Assignment(
            id = null,
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            score = (1..20).random(),
            startDate = LocalDate.of(2024, (1..7).random(), (1..20).random()),
            endDate = LocalDate.of(2024, (8..12).random(), (1..20).random()),
        )
    }

    @Test
    fun testDeleteAssignment(){
        //given
        val previousAssignment = makeTestAssignment()
        assignmentRepository.save(previousAssignment)
        val previousCount = assignmentRepository.count()

        //when
        assignmentService.delete(previousAssignment.id ?: 0)

        //then
        val afterCount = assignmentRepository.count()

        assertEquals(previousCount - 1, afterCount)

        if(afterCount > 0){
            val afterAssignment = assignmentRepository.findTopByOrderByIdDesc().orElseThrow()
            assertNotEquals(previousAssignment.id, afterAssignment.id)
            assertNotEquals(previousAssignment.title, afterAssignment.title)
            assertNotEquals(previousAssignment.description, afterAssignment.description)
        }

    }


}