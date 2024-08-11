package cobo.writing.assignment

import cobo.writing.data.dto.professor.ProfessorGetWritingListElementRes
import cobo.writing.data.entity.Assignment
import cobo.writing.data.entity.Writing
import cobo.writing.data.enums.WritingStateEnum
import cobo.writing.repository.AssignmentRepository
import cobo.writing.repository.WritingRepository
import cobo.writing.service.WritingService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertTrue

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProfessorGetWritingListTest @Autowired constructor(
    private val writingRepository: WritingRepository,
    private val assignmentRepository: AssignmentRepository,
    private val writingService: WritingService
){

    private val studentId = "TEST_STUDENT_ID"

    private val assignmentList = mutableListOf<Assignment>()
    private val writingList = mutableListOf<Writing>()

    @AfterEach
    fun clear(){
        writingRepository.deleteAll(writingList)
        writingList.clear()

        assignmentRepository.deleteAll(assignmentList)
        assignmentList.clear()
    }


    private fun makeTestAssignment(): Assignment {
        return Assignment(
            id = null,
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            score = (1..20).random(),
            startDate = LocalDate.of(2024, (1..7).random(), (1..20).random()),
            endDate = LocalDate.of(2024, (8..12).random(), (1..20).random()),
        )
    }

    private fun makeTestWriting(assignment: Assignment, state: Short): Writing {
        return Writing(
            id = null,
            studentId = studentId,
            assignment = assignment,
            content = UUID.randomUUID().toString(),
            state = WritingStateEnum.from(state) ?: WritingStateEnum.NOT_SUBMITTED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            submittedAt = LocalDateTime.now()
        )
    }

    @Test
    fun testGetWritingList(){
        //given
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val writing = makeTestWriting(assignment, WritingStateEnum.SUBMITTED.value)
        writingRepository.save(writing)
        writingList.add(writing)

        //when
        val professorGetWritingListRes = writingService.professorGetWritingList(
            assignmentId = assignment.id!!,
            page = 0,
            pageSize = 10
        )

        //then
        assertEquals(HttpStatus.OK, professorGetWritingListRes.statusCode)

        assertEquals(professorGetWritingListRes.body!!.data!!.totalElements, 1L)
        assertTrue(professorGetWritingListRes.body!!.data!!.writings.isNotEmpty())

        val expectedProfessorGetWritingListElementRes = ProfessorGetWritingListElementRes(
            studentId = writing.studentId,
            createdAt = writing.createdAt!!,
            updatedAt = writing.updatedAt!!,
            writingState = writing.state.value
        )

        assertEquals(expectedProfessorGetWritingListElementRes,
            professorGetWritingListRes.body!!.data!!.writings.last())
    }
}