package cobo.writing.assignment

import cobo.writing.data.dto.professor.ProfessorGetWriting
import cobo.writing.data.entity.Assignment
import cobo.writing.data.entity.Writing
import cobo.writing.data.enums.WritingStateEnum
import cobo.writing.repository.AssignmentRepository
import cobo.writing.repository.WritingRepository
import cobo.writing.service.WritingService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.roundToInt
import kotlin.test.assertEquals
import kotlin.test.assertNull

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProfessorGetWritingTest @Autowired constructor(
    private val assignmentRepository: AssignmentRepository,
    private val writingRepository: WritingRepository,
    private val writingService: WritingService
){

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
            prompt = UUID.randomUUID().toString()
        )
    }

    private fun makeTestWriting(studentId: String, assignment: Assignment, state: Short):Writing {
        return Writing(
            id = null,
            studentId = studentId,
            assignment = assignment,
            content = UUID.randomUUID().toString(),
            state = WritingStateEnum.from(state) ?: WritingStateEnum.NOT_SUBMITTED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            submittedAt = LocalDateTime.now(),
            score = (1..20).random(),
        )
    }

    private fun roundTo6Digits(localDateTime: LocalDateTime): LocalDateTime {
        val nano = localDateTime.nano
        val roundedNano = ((nano / 1_000.0).roundToInt() * 1_000)

        return localDateTime
            .truncatedTo(ChronoUnit.SECONDS)
            .plusNanos(roundedNano.toLong())
    }

    @Test
    fun testGetWritingValid(){
        //then
        val studentId = UUID.randomUUID().toString()

        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val testWriting = makeTestWriting(studentId, assignment, WritingStateEnum.SUBMITTED.value)
        writingRepository.save(testWriting)
        writingList.add(testWriting)

        //when
        val professorGetWritingRes = writingService.professorGetWriting(studentId = studentId, assignmentId = assignment.id!!)

        //then

        val expectedProfessorGetWriting = ProfessorGetWriting(
            content = testWriting.content,
            score = testWriting.score,
            createdAt = roundTo6Digits(testWriting.createdAt!!),
            writingState = WritingStateEnum.SUBMITTED.value
        )

        assertEquals(expectedProfessorGetWriting, professorGetWritingRes.body!!.data)
    }

    @Test
    fun testGetWritingInvalidAssignment() {
        //then
        val getProfessorWritingRes = writingService.professorGetWriting(assignmentId = Int.MAX_VALUE, studentId = UUID.randomUUID().toString())

        //when
        assertEquals(HttpStatus.OK, getProfessorWritingRes.statusCode)
        assertEquals("", getProfessorWritingRes.body!!.data!!.content)
        assertNull(getProfessorWritingRes.body!!.data!!.score)
        assertNull(getProfessorWritingRes.body!!.data!!.createdAt)
        assertEquals(0, getProfessorWritingRes.body!!.data!!.writingState.toInt())

    }

    @Test
    fun testGetWritingInvalidStudentId() {
        //given
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)


        //then
        val getProfessorWritingRes = writingService.professorGetWriting(assignmentId = assignment.id!!, studentId = UUID.randomUUID().toString())

        //when
        assertEquals(HttpStatus.OK, getProfessorWritingRes.statusCode)
        assertEquals("", getProfessorWritingRes.body!!.data!!.content)

    }
}