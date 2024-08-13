package cobo.writing.assignment

import cobo.writing.data.entity.Assignment
import cobo.writing.repository.AssignmentRepository
import cobo.writing.service.AssignmentService
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
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
            prompt = UUID.randomUUID().toString()
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

    @Test
    fun testDeleteInvalidAssignment(){
        //given
        val previousCount = assignmentRepository.count()

        //when
        try{
            assignmentService.delete(id = Int.MAX_VALUE / 2)
            assert(false)
        }catch(e: EntityNotFoundException){
            //then
            assertEquals(previousCount, assignmentRepository.count())
        }

    }

    @Test
    fun testDeleteAssignmentInMiddle(){
        //given
        val randomCount = (3..5).random()
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        for(i in 1..randomCount) {
            val curAssignment = makeTestAssignment()
            assignmentRepository.save(curAssignment)
            assignmentList.add(curAssignment)
        }

        val previousCount = assignmentRepository.count()

        //when
        val deleteAssignmentRes = assignmentService.delete(assignment.id ?: 0)

        //then

        assertEquals(HttpStatus.OK, deleteAssignmentRes.statusCode)
        assertEquals(previousCount - 1, assignmentRepository.count())

        val afterAssignment = assignmentRepository.findById(assignment.id ?: 0)

        assertTrue(afterAssignment.isEmpty)

        println(assignmentList)
    }
}