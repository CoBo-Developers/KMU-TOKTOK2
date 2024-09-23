package cobo.writing.student

import cobo.writing.data.dto.student.StudentGetRes
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentGetTest @Autowired constructor(
    private val assignmentRepository: AssignmentRepository,
    private val writingRepository : WritingRepository,
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

    private fun makeTestWriting(assignment: Assignment, state: Short):Writing {
        return Writing(
            id = null,
            studentId = studentId,
            assignment = assignment,
            content = UUID.randomUUID().toString(),
            state = WritingStateEnum.from(state) ?: WritingStateEnum.NOT_SUBMITTED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            submittedAt = LocalDateTime.now(),
            score = assignment.score ?: 0,
        )
    }

    private fun makeTestStudent(studentId: String): SecurityContext {
        val securityContextHolder = SecurityContextHolder.getContext()
        securityContextHolder.authentication = UsernamePasswordAuthenticationToken(
            studentId, null, listOf(
                SimpleGrantedAuthority("STUDENT")
            ))
        return securityContextHolder
    }

    private fun testStudentGetWithWritingState(writingStateEnum: WritingStateEnum){
        //given
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val writing = makeTestWriting(assignment, writingStateEnum.value)
        writingRepository.save(writing)
        writingList.add(writing)

        val securityContext = makeTestStudent(studentId)

        //when
        val studentGetRes = writingService.studentGet(assignment.id!!, securityContext.authentication)

        //then
        assertEquals(HttpStatus.OK, studentGetRes.statusCode)

        val savedWriting = writingRepository.findTopByOrderByIdDesc().orElseThrow()

        val expectedStudentGetRes = StudentGetRes(
            assignmentId = savedWriting.assignment.id!!,
            content = savedWriting.content,
        )

        assertEquals(expectedStudentGetRes, studentGetRes.body!!.data)
    }

    @Test
    fun testStudentGetWithSubmitted(){
        testStudentGetWithWritingState(WritingStateEnum.SUBMITTED)
    }

    @Test
    fun testStudentGetWithNotApproved(){
        testStudentGetWithWritingState(WritingStateEnum.NOT_APPROVED)
    }

    @Test
    fun testStudentGetWithApproved(){
        testStudentGetWithWritingState(WritingStateEnum.APPROVED)
    }

    @Test
    fun testStudentGetNotSubmitted(){
        //given
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val securityContext = makeTestStudent(studentId)

        //when
        val studentGetRes = writingService.studentGet(assignment.id!!, securityContext.authentication)

        //then
        assertEquals(HttpStatus.OK, studentGetRes.statusCode)

        val expectedStudentGetRes = StudentGetRes(
            assignmentId = assignment.id!!,
            content = "",
        )

        assertEquals(expectedStudentGetRes, studentGetRes.body!!.data)
    }

    @Test
    fun testStudentGetInvalidAssignment(){
        //given
        val securityContext = makeTestStudent(studentId)
        val assignmentId = Int.MAX_VALUE

        //when
        val studentGetRes = writingService.studentGet(assignmentId, securityContext.authentication)

        //then
        assertEquals(HttpStatus.OK, studentGetRes.statusCode)

        val expectedStudentGetRes = StudentGetRes(
            assignmentId = assignmentId,
            content = "",
        )

        assertEquals(expectedStudentGetRes, studentGetRes.body!!.data)
    }
}