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

    private fun makeTestWriting(assignment: Assignment, state: Short, studentId: String): Writing {
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

        val writing = makeTestWriting(assignment, WritingStateEnum.SUBMITTED.value, studentId)
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

    @Test
    fun testGetWritingListMoreThanOneElement(){
        //given
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val randomCount = (5..7).random()

        for(i in 1..randomCount) {
            val writing = makeTestWriting(assignment, (1..3).random().toShort(), "${studentId}${i}")
            writingRepository.save(writing)
            writingList.add(writing)
        }

        writingList.sortBy{ it.state }

        //when
        val professorGetWritingListRes = writingService.professorGetWritingList(assignment.id!!, 0, randomCount)

        //then

        assertEquals(HttpStatus.OK, professorGetWritingListRes.statusCode)

        assertEquals(professorGetWritingListRes.body!!.data!!.totalElements, randomCount.toLong())

        for (i in 0 ..< randomCount) {


            val expectedProfessorGetWritingListElementRes =
                ProfessorGetWritingListElementRes(
                    studentId = writingList[i].studentId,
                    createdAt = writingList[i].createdAt!!,
                    updatedAt = writingList[i].updatedAt!!,
                    writingState = writingList[i].state.value
                )
            println(expectedProfessorGetWritingListElementRes)

            assertEquals(expectedProfessorGetWritingListElementRes, professorGetWritingListRes.body!!.data!!.writings[i])
        }
    }

    @Test
    fun testGetWritingListAdd(){
        //given
        val student1 = studentId + "1"
        val student2 = studentId + "2"
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val writing1 = makeTestWriting(assignment, WritingStateEnum.APPROVED.value, student1)
        writingRepository.save(writing1)
        writingList.add(writing1)

        val previousCount = writingRepository.countByAssignmentIdWithJDBC(assignmentId = assignment.id!!)

        //when
        val writing2 = makeTestWriting(assignment, WritingStateEnum.SUBMITTED.value, student2)
        writingRepository.save(writing2)
        writingList.add(writing2)

        val professorGetWritingListRes = writingService.professorGetWritingList(assignment.id!!, 0, 2)

        //then

        assertEquals(HttpStatus.OK, professorGetWritingListRes.statusCode)

        assertEquals(professorGetWritingListRes.body!!.data!!.totalElements, 2L)

        assertEquals(previousCount + 1, professorGetWritingListRes.body!!.data!!.totalElements)

        val expectedProfessorGetWritingListElementRes1 = ProfessorGetWritingListElementRes(
            studentId = student1,
            createdAt = writing1.createdAt!!,
            updatedAt = writing1.updatedAt!!,
            writingState = writing1.state.value
        )

        val expectedProfessorGetWritingListElementRes2 = ProfessorGetWritingListElementRes(
            studentId = student2,
            createdAt = writing2.createdAt!!,
            updatedAt = writing2.updatedAt!!,
            writingState = writing2.state.value
        )

        assertEquals(expectedProfessorGetWritingListElementRes1,
            professorGetWritingListRes.body!!.data!!.writings[1])
        assertEquals(expectedProfessorGetWritingListElementRes2,
            professorGetWritingListRes.body!!.data!!.writings[0])
    }

    @Test
    fun testGetWritingListPaging(){
        //given
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        val randomCount = (5..7).random()
        for(i in 1..randomCount) {
            val writing = makeTestWriting(assignment, (1..3).random().toShort(), "${studentId}${i}")
            writingRepository.save(writing)
            writingList.add(writing)
        }

        writingList.sortBy{ it.state }

        //then
        for (i in 0..<randomCount) {
            val professorGetWritingListRes = writingService.professorGetWritingList(assignment.id!!, i, 1)

            //then

            assertEquals(HttpStatus.OK, professorGetWritingListRes.statusCode)

            assertEquals(professorGetWritingListRes.body!!.data!!.totalElements, randomCount.toLong())

            assertEquals(professorGetWritingListRes.body!!.data!!.writings.size, 1)

            val expectedProfessorGetWritingListElementRes =
                ProfessorGetWritingListElementRes(
                    studentId = writingList[i].studentId,
                    createdAt = writingList[i].createdAt!!,
                    updatedAt = writingList[i].updatedAt!!,
                    writingState = writingList[i].state.value
                )


            assertEquals(
                expectedProfessorGetWritingListElementRes,
                professorGetWritingListRes.body!!.data!!.writings.last()
            )
        }

    }
}