package cobo.writing.assignment

import cobo.writing.data.dto.professor.AssignmentPutReq
import cobo.writing.data.entity.Assignment
import cobo.writing.repository.AssignmentRepository
import cobo.writing.service.AssignmentService
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AssignmentPutTest @Autowired constructor(
    private val assignmentRepository : AssignmentRepository,
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
     fun testPutAssignment(){
         //given
         val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val previousCount = assignmentRepository.count()

        val assignmentPutReq = AssignmentPutReq(
            id = assignment.id ?: throw NullPointerException("id"),
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            score = (1..20).random(),
            startDate = LocalDate.of(2024, (1..7).random(), (1..20).random()),
            endDate = LocalDate.of(2024, (8..12).random(), (1..20).random()),
            prompt = UUID.randomUUID().toString()
        )

        //when
        val putAssignment = assignmentService.put(assignmentPutReq)

        //then
        assertEquals(HttpStatus.OK, putAssignment.statusCode)

        val afterCount = assignmentRepository.count()

        val newAssignment = assignmentRepository.findById(assignment.id!!).orElseThrow()
        assertEquals(assignment.id, newAssignment.id)
        assertEquals(assignmentPutReq.title, newAssignment.title)
        assertEquals(assignmentPutReq.description, newAssignment.description)
        assertEquals(assignmentPutReq.score, newAssignment.score)
        assertEquals(assignmentPutReq.startDate, newAssignment.startDate)
        assertEquals(assignmentPutReq.endDate, newAssignment.endDate)
        assertEquals(assignmentPutReq.prompt, newAssignment.prompt)

        assertEquals(previousCount, afterCount)
    }

    @Test
    fun testPutAssignmentInMiddle(){
        //given
        val startCount = (1..3).random()
        for (i in 1..startCount) {
            val curAssignment = makeTestAssignment()
            assignmentList.add(assignmentRepository.save(curAssignment))
        }

        val assignment = assignmentRepository.save(makeTestAssignment())
        assignmentList.add(assignment)

        val endCount = (1..3).random()
        for(i in 1..endCount) {
            val curAssignment = makeTestAssignment()
            assignmentList.add(assignmentRepository.save(curAssignment))
        }

        //when
        val assignmentPutReq = AssignmentPutReq(
            id = assignment.id ?: throw NullPointerException("id"),
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            score = (1..20).random(),
            startDate = LocalDate.of(2024, (1..7).random(), (1..20).random()),
            endDate = LocalDate.of(2024, (8..12).random(), (1..20).random()),
            prompt = UUID.randomUUID().toString()
        )
        assignmentService.put(assignmentPutReq)

        //then

        val newAssignment = assignmentRepository.findById(assignment.id!!).orElseThrow()

        assertEquals(assignment.id, newAssignment.id)
        assertEquals(assignmentPutReq.title, newAssignment.title)
        assertEquals(assignmentPutReq.description, newAssignment.description)
        assertEquals(assignmentPutReq.score, newAssignment.score)
        assertEquals(assignmentPutReq.startDate, newAssignment.startDate)
        assertEquals(assignmentPutReq.endDate, newAssignment.endDate)
        assertEquals(assignmentPutReq.prompt, newAssignment.prompt)

    }

    @Test
    fun testPutAssignmentWithNotValidId(){
        //given
        val assignmentPutReq = AssignmentPutReq(
            id = Int.MAX_VALUE / 2,
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            score = (1..20).random(),
            startDate = LocalDate.of(2024, (1..7).random(), (1..20).random()),
            endDate = LocalDate.of(2024, (8..12).random(), (1..20).random()),
            prompt = UUID.randomUUID().toString()
        )

        //when
        try{
            assignmentService.put(assignmentPutReq)
            assert(false)
        }catch(entityNotFoundException: EntityNotFoundException){
            assert(true)
        }
    }
}