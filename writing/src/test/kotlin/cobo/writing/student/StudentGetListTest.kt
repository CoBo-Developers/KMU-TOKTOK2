package cobo.writing.student

import cobo.writing.data.dto.student.StudentGetListResElement
import cobo.writing.data.entity.Assignment
import cobo.writing.data.entity.Writing
import cobo.writing.data.enums.WritingStateEnum
import cobo.writing.repository.AssignmentRepository
import cobo.writing.repository.WritingRepository
import cobo.writing.service.AssignmentService
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
import kotlin.test.assertNotNull

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentGetListTest @Autowired constructor(
    private val assignmentRepository: AssignmentRepository,
    private val writingRepository : WritingRepository,
    private val assignmentService: AssignmentService
) {

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
            submittedAt = LocalDateTime.now()
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

    private fun testStudentGetListWithState(state: Short){
        //given
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val writing = makeTestWriting(assignment, state)
        writingRepository.save(writing)
        writingList.add(writing)

        val securityContextHolder = makeTestStudent(studentId)

        //when
        val studentGetListRes = assignmentService.studentGetList(securityContextHolder.authentication)

        //then

        assertEquals(HttpStatus.OK, studentGetListRes.statusCode)

        assertNotNull(studentGetListRes.body!!.data)

        val expectedStudentGetListResElement =
            StudentGetListResElement(
                id = assignment.id!!,
                title = assignment.title!!,
                description = assignment.description!!,
                score = assignment.score,
                startDate = assignment.startDate,
                endDate = assignment.endDate,
                writingState = state
            )

        assertEquals(expectedStudentGetListResElement, studentGetListRes.body!!.data!!.assignmentList.last())
    }

    @Test
    fun testStudentGetListWithNoData(){
        testStudentGetListWithState(0)
    }


    @Test
    fun testStudentGetListWithNotSubmitted(){
        testStudentGetListWithState(WritingStateEnum.NOT_SUBMITTED.value)
    }

}