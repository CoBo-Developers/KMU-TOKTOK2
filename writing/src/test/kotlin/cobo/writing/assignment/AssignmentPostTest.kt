package cobo.writing.assignment

import cobo.writing.data.dto.professor.AssignmentPostReq
import cobo.writing.data.entity.Assignment
import cobo.writing.repository.AssignmentRepository
import cobo.writing.service.AssignmentService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
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
class AssignmentPostTest @Autowired constructor(
    private val assignmentRepository: AssignmentRepository,
    private val assignmentService: AssignmentService
) {

    private val assignmentList = mutableListOf<Assignment>()

    @AfterEach
    fun clear(){
        assignmentRepository.deleteAll(assignmentList)
        assignmentList.clear()
    }

    @Test
    fun testPost(){
        //given

        val assignment = Assignment(
            id = null,
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            score = (1..10).random(),
            startDate = LocalDate.of(2024, (1..6).random(), (1..30).random()),
            endDate = LocalDate.of(2024, (7..12).random(), (1..30).random()),
            prompt = UUID.randomUUID().toString(),
        )

        val assignmentPostReq = AssignmentPostReq(
            title = assignment.title ?: "",
            description = assignment.description ?: "",
            score = assignment.score!!,
            startDate = assignment.startDate!!,
            endDate = assignment.endDate!!,
            prompt = assignment.prompt ?: "",
        )

        //when
        val postRes = assignmentService.post(assignmentPostReq)


        //then
        assertEquals(HttpStatus.CREATED, postRes.statusCode)

        val optionalSavedAssignment = assignmentRepository.findTopByOrderByIdDesc()

        assertTrue(optionalSavedAssignment.isPresent)
        val savedAssignment = optionalSavedAssignment.get()

        assertEquals(assignment.title, savedAssignment.title)
        assertEquals(assignment.description, savedAssignment.description)
        assertEquals(assignment.score, savedAssignment.score)
        assertEquals(assignment.startDate, savedAssignment.startDate)
        assertEquals(assignment.endDate, savedAssignment.endDate)
        assertEquals(assignment.prompt, savedAssignment.prompt)
        assignmentList.add(savedAssignment)
    }

    @Test
    fun testPostInMiddle(){
        //given
        val assignment = Assignment(
            id = null,
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            score = (1..10).random(),
            startDate = LocalDate.of(2024, (1..6).random(), (1..30).random()),
            endDate = LocalDate.of(2024, (7..12).random(), (1..30).random()),
            prompt = UUID.randomUUID().toString(),
        )

        val startRandomCount = (5..10).random()
        for(i in 1 .. startRandomCount) {
            val curAssignment = Assignment(
                id = null,
                title = UUID.randomUUID().toString(),
                description = UUID.randomUUID().toString(),
                score = (1..10).random(),
                startDate = LocalDate.of(2024, (1..6).random(), (1..20).random()),
                endDate = LocalDate.of(2024, (7..12).random(), (1..20).random()),
                prompt = UUID.randomUUID().toString(),
            )
            assignmentList.add(curAssignment)
        }
        assignmentRepository.saveAll(assignmentList.subList(0, startRandomCount))


        val endRandomCount = (5..10).random()
        for(i in 1 .. endRandomCount) {
            val curAssignment = Assignment(
                id = null,
                title = UUID.randomUUID().toString(),
                description = UUID.randomUUID().toString(),
                score = (1..10).random(),
                startDate = LocalDate.of(2024, (1..6).random(), (1..20).random()),
                endDate = LocalDate.of(2024, (7..12).random(), (1..20).random()),
                prompt = UUID.randomUUID().toString(),
            )
            assignmentList.add(curAssignment)
        }
        //when
        val assignmentPostReq = AssignmentPostReq(
            title = assignment.title ?: "",
            description = assignment.description ?: "",
            score = assignment.score!!,
            startDate = assignment.startDate!!,
            endDate = assignment.endDate!!,
            prompt = assignment.prompt ?: "",
        )

        assignmentService.post(assignmentPostReq)

        assignmentRepository.saveAll(assignmentList.subList(startRandomCount, startRandomCount + endRandomCount))


        //then
        val savedAssignmentList = assignmentRepository.findAll()

        val targetAssignment = savedAssignmentList[savedAssignmentList.size - 1 - endRandomCount]

        assertEquals(assignment.title, targetAssignment.title)
        assertEquals(assignment.description, targetAssignment.description)
        assertEquals(assignment.score, targetAssignment.score)
        assertEquals(assignment.startDate, targetAssignment.startDate)
        assertEquals(assignment.endDate, targetAssignment.endDate)
        assertEquals(assignment.prompt, targetAssignment.prompt)

        assignmentList.add(targetAssignment)
    }

    @Test
    fun testPostInvalidDate(){
        //given
        val assignment = Assignment(
            id = null,
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            score = (1..10).random(),
            startDate = LocalDate.of(2024, (7..12).random(), (1..30).random()),
            endDate = LocalDate.of(2024, (1..6).random(), (1..30).random()),
            prompt = UUID.randomUUID().toString()
        )
        val assignmentPostReq = AssignmentPostReq(
            title = assignment.title ?: "",
            description = assignment.description ?: "",
            score = assignment.score!!,
            startDate = assignment.startDate!!,
            endDate = assignment.endDate!!,
            prompt = UUID.randomUUID().toString(),
        )


        //when
        val postRes = assignmentService.post(assignmentPostReq)

        //then
        assertEquals(HttpStatus.BAD_REQUEST, postRes.statusCode)
    }
}