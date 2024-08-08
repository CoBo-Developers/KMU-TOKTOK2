package cobo.writing.student

import cobo.writing.data.dto.student.StudentPostReq
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
import java.time.ZoneId
import java.util.*
import kotlin.test.assertTrue

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentPostTest @Autowired constructor(
    private val assignmentRepository: AssignmentRepository,
    private val writingRepository : WritingRepository,
    private val writingService: WritingService
) {

    private val assignmentList = mutableListOf<Assignment>()
    private val writingList = mutableListOf<Writing>()

    @AfterEach
    fun assignmentClear(){
        writingRepository.deleteAll(writingList)
        writingList.clear()

        assignmentRepository.deleteAll(assignmentList)
        assignmentList.clear()
    }

    private val studentId = "TEST_STUDENT_ID"

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

    @Test
    fun testValidTime(){
        val koreaTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        val curTime = LocalDateTime.now()

        println("KOREA TIME: $koreaTime")
        println("SERVER TIME: $koreaTime")

        assertTrue(koreaTime.minusMinutes(5).isBefore(curTime))
        assertTrue(koreaTime.plusMinutes(5).isAfter(curTime))
    }

    @Test
    fun testStudentPost(){
        //given
        val startTime = LocalDateTime.now()
        val assignment = makeTestAssignment()
        assignment.startDate = LocalDate.now().minusDays(1)
        assignment.endDate = LocalDate.now().plusDays(1)

        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val securityContext = makeTestStudent(studentId = studentId)

        val testContent = UUID.randomUUID().toString()

        //when
        val studentPostReq = StudentPostReq(
            assignmentId = assignment.id!!,
            content = testContent,
            writingState = WritingStateEnum.SUBMITTED.value,
        )
        val studentPostRes = writingService.studentPost(studentPostReq, securityContext.authentication)

        val endTime = LocalDateTime.now()

        //then
        assertEquals(HttpStatus.CREATED, studentPostRes.statusCode)

        val optionalWriting = writingRepository.findTopByOrderByIdDesc()

        assertTrue(optionalWriting.isPresent)

        val writing = optionalWriting.get()

        val expectedWriting = Writing(
            id = writing.id,
            studentId = studentId,
            assignment = assignment,
            content = testContent,
            state = WritingStateEnum.SUBMITTED,
            createdAt = writing.createdAt,
            updatedAt = writing.updatedAt,
            submittedAt = writing.submittedAt
        )

        assertEquals(expectedWriting, writing)

        assert((writing.createdAt!!.isAfter(startTime) || writing.createdAt!!.isEqual(startTime))&& (writing.createdAt!!.isBefore(endTime) || writing.createdAt!!.isEqual(endTime)))
        assert((writing.updatedAt!!.isAfter(startTime) || writing.updatedAt!!.isEqual(startTime))&& (writing.updatedAt!!.isBefore(endTime) || writing.updatedAt!!.isEqual(endTime)))
        assert((writing.submittedAt!!.isAfter(startTime) || writing.submittedAt!!.isEqual(startTime))&& (writing.submittedAt!!.isBefore(endTime) || writing.submittedAt!!.isEqual(endTime)))

        writingList.add(writing)
    }

    @Test
    fun testStudentPostWithInvalidState(){
        listOf(0, 2, 3).forEach{
            //given
            val assignment = makeTestAssignment()
            assignment.startDate = LocalDate.now().minusDays(1)
            assignment.endDate = LocalDate.now().plusDays(1)

            assignmentRepository.save(assignment)
            assignmentList.add(assignment)

            val securityContext = makeTestStudent(studentId = studentId)

            val testContent = UUID.randomUUID().toString()

            //when
            val studentPostReq = StudentPostReq(
                assignmentId = assignment.id!!,
                content = testContent,
                writingState = it.toShort(),
            )
            val studentPostRes = writingService.studentPost(studentPostReq, securityContext.authentication)

            //then
            assertEquals(HttpStatus.BAD_REQUEST, studentPostRes.statusCode)
        }
    }

    @Test
    fun testStudentPostWithStartDateToday(){
        //given
        val assignment = makeTestAssignment()
        assignment.startDate = LocalDate.now()
        assignment.endDate = LocalDate.now().plusDays(1)

        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val securityContext = makeTestStudent(studentId = studentId)

        val testContent = UUID.randomUUID().toString()

        //when
        val studentPostReq = StudentPostReq(
            assignmentId = assignment.id!!,
            content = testContent,
            writingState = WritingStateEnum.SUBMITTED.value,
        )
        val studentPostRes = writingService.studentPost(studentPostReq, securityContext.authentication)


        //then
        assertEquals(HttpStatus.CREATED, studentPostRes.statusCode)

        val optionalWriting = writingRepository.findTopByOrderByIdDesc()

        assertTrue(optionalWriting.isPresent)

        val writing = optionalWriting.get()

        val expectedWriting = Writing(
            id = writing.id,
            studentId = studentId,
            assignment = assignment,
            content = testContent,
            state = WritingStateEnum.SUBMITTED,
            createdAt = writing.createdAt,
            updatedAt = writing.updatedAt,
            submittedAt = writing.submittedAt
        )

        assertEquals(expectedWriting, writing)

        writingList.add(writing)
    }

    @Test
    fun testStudentPostWithEndDateToday(){
        //given
        val assignment = makeTestAssignment()
        assignment.startDate = LocalDate.now().minusDays(1)
        assignment.endDate = LocalDate.now()

        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val securityContext = makeTestStudent(studentId = studentId)

        val testContent = UUID.randomUUID().toString()

        //when
        val studentPostReq = StudentPostReq(
            assignmentId = assignment.id!!,
            content = testContent,
            writingState = WritingStateEnum.SUBMITTED.value,
        )
        val studentPostRes = writingService.studentPost(studentPostReq, securityContext.authentication)


        //then
        assertEquals(HttpStatus.CREATED, studentPostRes.statusCode)

        val optionalWriting = writingRepository.findTopByOrderByIdDesc()

        assertTrue(optionalWriting.isPresent)

        val writing = optionalWriting.get()

        val expectedWriting = Writing(
            id = writing.id,
            studentId = studentId,
            assignment = assignment,
            content = testContent,
            state = WritingStateEnum.SUBMITTED,
            createdAt = writing.createdAt,
            updatedAt = writing.updatedAt,
            submittedAt = writing.submittedAt
        )

        assertEquals(expectedWriting, writing)

        writingList.add(writing)
    }

    @Test
    fun testStudentPostWithEndDateYesterday(){
        //given
        val assignment = makeTestAssignment()
        assignment.startDate = LocalDate.now().minusDays(2)
        assignment.endDate = LocalDate.now().minusDays(1)

        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val securityContext = makeTestStudent(studentId = studentId)

        val testContent = UUID.randomUUID().toString()

        //when
        val studentPostReq = StudentPostReq(
            assignmentId = assignment.id!!,
            content = testContent,
            writingState = WritingStateEnum.SUBMITTED.value,
        )
        val studentPostRes = writingService.studentPost(studentPostReq, securityContext.authentication)

        //then
        assertEquals(HttpStatus.BAD_REQUEST, studentPostRes.statusCode)
    }

    @Test
    fun testStudentPostWithStartDateTomorrow(){
        //given
        val assignment = makeTestAssignment()
        assignment.startDate = LocalDate.now().plusDays(1)
        assignment.endDate = LocalDate.now().plusDays(2)

        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val securityContext = makeTestStudent(studentId = studentId)

        val testContent = UUID.randomUUID().toString()

        //when
        val studentPostReq = StudentPostReq(
            assignmentId = assignment.id!!,
            content = testContent,
            writingState = WritingStateEnum.SUBMITTED.value,
        )
        val studentPostRes = writingService.studentPost(studentPostReq, securityContext.authentication)

        //then
        assertEquals(HttpStatus.BAD_REQUEST, studentPostRes.statusCode)
    }

    @Test
    fun testStudentPostWithInvalidAssignment(){
        //given
        val securityContext = makeTestStudent(studentId = studentId)
        val testContent = UUID.randomUUID().toString()
        val studentPostReq = StudentPostReq(
            assignmentId = Int.MAX_VALUE,
            content = testContent,
            writingState = WritingStateEnum.SUBMITTED.value,
        )

        //when

        val studentPostRes = writingService.studentPost(studentPostReq, securityContext.authentication)

        //then
        assertEquals(HttpStatus.NOT_FOUND, studentPostRes.statusCode)
    }
}