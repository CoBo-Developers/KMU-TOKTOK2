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
            score = (1..20).random(),
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
                score = assignment.score!!,
                startDate = assignment.startDate!!,
                endDate = assignment.endDate!!,
                writingState = state,
                writingScore = writing.score!!
            )

        assertEquals(expectedStudentGetListResElement, studentGetListRes.body!!.data!!.assignmentList.last())
    }

    @Test
    fun testStudentGetListWithNoData(){
        //given
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

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
                score = assignment.score!!,
                startDate = assignment.startDate!!,
                endDate = assignment.endDate!!,
                writingState = 0,
                writingScore = 0
            )

        assertEquals(expectedStudentGetListResElement, studentGetListRes.body!!.data!!.assignmentList.last())
    }


    @Test
    fun testStudentGetListWithNotSubmitted(){
        testStudentGetListWithState(WritingStateEnum.NOT_SUBMITTED.value)
    }

    @Test
    fun testStudentGetListWithSubmitted(){
        testStudentGetListWithState(WritingStateEnum.SUBMITTED.value)
    }

    @Test
    fun testStudentGetListMoreThanOneElement(){
        //given
        val randomCount = (5..7).random()
        val writingFlagList = mutableListOf<Boolean>()
        for(i in 1 .. randomCount) {
            val writingFlag = Random().nextBoolean()
            writingFlagList.add(writingFlag)

            val assignment = makeTestAssignment()
            assignmentRepository.save(assignment)
            assignmentList.add(assignment)

            if (writingFlag) {
                val writing = makeTestWriting(assignment, (1..2).random().toShort())

                writingRepository.save(writing)
                writingList.add(writing)
            }
        }

        val securityContextHolder = makeTestStudent(studentId)

        //when
        val studentGetListRes = assignmentService.studentGetList(securityContextHolder.authentication)
        val studentGetList = studentGetListRes.body!!.data!!.assignmentList.subList(
            studentGetListRes.body!!.data!!.assignmentList.size - randomCount,
            studentGetListRes.body!!.data!!.assignmentList.size)

        //then
        assertEquals(HttpStatus.OK, studentGetListRes.statusCode)

        assertNotNull(studentGetListRes.body!!.data)
        var writingIndex = 0

        for(i in 0 ..< randomCount) {

            val expectedStudentGetListResElement = StudentGetListResElement(
                id = assignmentList[i].id!!,
                title = assignmentList[i].title!!,
                description = assignmentList[i].description!!,
                score = assignmentList[i].score!!,
                startDate = assignmentList[i].startDate!!,
                endDate = assignmentList[i].endDate!!,
                writingState = if(writingFlagList[i]){
                    writingList[writingIndex].state.value
                }else{
                    0
                },
                writingScore = if(writingFlagList[i]){
                    writingList[writingIndex++].score!!
                }else{
                    0
                }
            )
            assertEquals(expectedStudentGetListResElement, studentGetList[i])
        }
    }

    @Test
    fun testStudentGetListWithDeletedAssignment(){
        //given
        val securityContextHolder = makeTestStudent(studentId)
        val before = assignmentService.studentGetList(securityContextHolder.authentication)
        val assignment = makeTestAssignment()
        assignment.deleted = true
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)


        //when
        val studentGetListRes = assignmentService.studentGetList(securityContextHolder.authentication)

        //then
        assertEquals(HttpStatus.OK, studentGetListRes.statusCode)
        assertNotNull(studentGetListRes.body!!.data)

        assertEquals(before.body!!.data!!.assignmentList.size, studentGetListRes.body!!.data!!.assignmentList.size)

        if(studentGetListRes.body!!.data!!.assignmentList.isNotEmpty()){
            assertEquals(before.body!!.data!!.assignmentList.last(), studentGetListRes.body!!.data!!.assignmentList.last())
        }
    }

}