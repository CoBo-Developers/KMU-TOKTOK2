package cobo.writing.assignment

import cobo.writing.data.dto.professor.AssignmentPatchWritingReq
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
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProfessorPathWritingTest @Autowired constructor(
    private val assignmentRepository : AssignmentRepository,
    private val writingRepository : WritingRepository,
    private val writingService: WritingService
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

    private fun patchWritingWithState(
        startWritingState: WritingStateEnum,
        endWritingState: WritingStateEnum,
        score: Int){
        //given
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val writing = makeTestWriting(assignment, startWritingState.value)
        writingRepository.save(writing)
        writingList.add(writing)

        //when
        val assignmentPatchReq = AssignmentPatchWritingReq(
            assignmentId = assignment.id!!,
            studentId = studentId,
            writingState = endWritingState.value,
            score = score
        )
        val patchWritingRes = writingService.assignmentPatchWriting(assignmentPatchReq)

        //then
        assertEquals(HttpStatus.OK, patchWritingRes.statusCode)

        val savedWriting = writingRepository.findTopByOrderByIdDesc().orElseThrow()

        assertEquals(endWritingState, savedWriting.state)
        assertEquals(savedWriting.score, savedWriting.score)
    }

    @Test
    fun testPatchWritingWithSubmittedToSubmitted(){
        patchWritingWithState(
            startWritingState = WritingStateEnum.SUBMITTED,
            endWritingState = WritingStateEnum.SUBMITTED,
            score = (1..20).random()
        )
    }

    @Test
    fun testPatchWritingWithSubmittedToApproved(){
        patchWritingWithState(
            startWritingState = WritingStateEnum.SUBMITTED,
            endWritingState = WritingStateEnum.APPROVED,
            score = (1..20).random()
        )
    }

    @Test
    fun testPatchWritingWithSubmittedToNotApproved(){
        patchWritingWithState(
            startWritingState = WritingStateEnum.SUBMITTED,
            endWritingState = WritingStateEnum.NOT_APPROVED,
            score = (1..20).random()
        )
    }

    @Test
    fun testPatchWritingWithApprovedToSubmitted(){
        patchWritingWithState(
            startWritingState = WritingStateEnum.APPROVED,
            endWritingState = WritingStateEnum.SUBMITTED,
            score = (1..20).random()
        )
    }

    @Test
    fun testPatchWritingWithApprovedToApproved(){
        patchWritingWithState(
            startWritingState = WritingStateEnum.APPROVED,
            endWritingState = WritingStateEnum.APPROVED,
            score = (1..20).random()
        )
    }

    @Test
    fun testPatchWritingWithApprovedToNotApproved(){
        patchWritingWithState(
            startWritingState = WritingStateEnum.APPROVED,
            endWritingState = WritingStateEnum.NOT_APPROVED,
            score = (1..20).random()
        )
    }

    @Test
    fun testPatchWritingWithNotApprovedToSubmitted(){
        patchWritingWithState(
            startWritingState = WritingStateEnum.NOT_APPROVED,
            endWritingState = WritingStateEnum.SUBMITTED,
            score = (1..20).random()
        )
    }

    @Test
    fun testPatchWritingWithNotApprovedToApproved(){
        patchWritingWithState(
            startWritingState = WritingStateEnum.NOT_APPROVED,
            endWritingState = WritingStateEnum.APPROVED,
            score = (1..20).random()
        )
    }

    @Test
    fun testPatchWritingWithNotApprovedToNotApproved(){
        patchWritingWithState(
            startWritingState = WritingStateEnum.NOT_APPROVED,
            endWritingState = WritingStateEnum.NOT_APPROVED,
            score = (1..20).random()
        )
    }

    @Test
    fun testPatchWithInvalidWriting(){
        //given
        val assignment = makeTestAssignment()
        assignment.id = Int.MAX_VALUE

        //when
        val assignmentPatchReq = AssignmentPatchWritingReq(
            assignmentId = Int.MAX_VALUE,
            studentId = studentId,
            writingState = WritingStateEnum.SUBMITTED.value,
            score = (1..20).random()
        )
        val patchWritingRes = writingService.assignmentPatchWriting(assignmentPatchReq)

        //then
        assertEquals(HttpStatus.NOT_FOUND, patchWritingRes.statusCode)

    }

    @Test
    fun testPatchWithInvalidState(){
        //given
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val writing = makeTestWriting(assignment, WritingStateEnum.SUBMITTED.value)
        writingRepository.save(writing)
        writingList.add(writing)

        //when
        val assignmentPatchReq = AssignmentPatchWritingReq(
            assignmentId = assignment.id!!,
            studentId = studentId,
            writingState = Short.MAX_VALUE,
            score = (1..20).random()
        )
        val patchWritingRes = writingService.assignmentPatchWriting(assignmentPatchReq)

        //then
        assertEquals(HttpStatus.BAD_REQUEST, patchWritingRes.statusCode)

    }

}