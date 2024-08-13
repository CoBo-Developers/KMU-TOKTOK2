package cobo.writing.assignment

import cobo.writing.data.entity.Assignment
import cobo.writing.data.entity.Writing
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
import java.util.*
import kotlin.test.assertEquals

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

    @Test
    fun testGetWritingInvalidAssignment() {
        //then
        val getProfessorWritingRes = writingService.professorGetWriting(assignmentId = Int.MAX_VALUE, studentId = UUID.randomUUID().toString())

        //when
        assertEquals(HttpStatus.OK, getProfessorWritingRes.statusCode)
        assertEquals("", getProfessorWritingRes.body!!.data!!.content)

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