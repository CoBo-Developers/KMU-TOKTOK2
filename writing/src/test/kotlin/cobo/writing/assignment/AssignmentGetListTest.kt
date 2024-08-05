package cobo.writing.assignment

import cobo.writing.data.entity.Assignment
import cobo.writing.repository.AssignmentRepository
import cobo.writing.service.AssignmentService
import org.hibernate.validator.internal.util.Contracts.assertNotNull
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
class AssignmentGetListTest @Autowired constructor(
    private val assignmentRepository : AssignmentRepository,
    private val assignmentService: AssignmentService
) {

    private val assignmentList = mutableListOf<Assignment>()

    @AfterEach
    fun clear(){
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

    @Test
    fun testGetListOneElement(){
        //given
        val count = assignmentRepository.count()
        val assignment = makeTestAssignment()
        assignmentRepository.save(assignment)
        assignmentList.add(assignment)

        //when
        val assignmentGetListRes = assignmentService.getList()

        //then
        assertEquals(HttpStatus.OK, assignmentGetListRes.statusCode)
        assertNotNull(assignmentGetListRes.body)
        assertNotNull(assignmentGetListRes.body!!.data)

        assertEquals(count + 1, assignmentGetListRes.body!!.data!!.assignments.size.toLong())

        assertEquals(assignment.id, assignmentGetListRes.body!!.data!!.assignments.last().id)
        assertEquals(assignment.title, assignmentGetListRes.body!!.data!!.assignments.last().title)
        assertEquals(assignment.description, assignmentGetListRes.body!!.data!!.assignments.last().description)
        assertEquals(assignment.score, assignmentGetListRes.body!!.data!!.assignments.last().score)
        assertEquals(assignment.startDate, assignmentGetListRes.body!!.data!!.assignments.last().startDate)
        assertEquals(assignment.endDate, assignmentGetListRes.body!!.data!!.assignments.last().endDate)
    }

    @Test
    fun testGetListMoreThanOneElement(){
        //given
        val count = assignmentRepository.count()
        val randomCount = (5..10).random()
        for (i in 1..randomCount) {
            val assignment = makeTestAssignment()
            assignmentRepository.save(assignment)
            assignmentList.add(assignment)
        }

        //when
        val assignmentGetListRes = assignmentService.getList()

        //then
        assertEquals(HttpStatus.OK, assignmentGetListRes.statusCode)
        assertNotNull(assignmentGetListRes.body)
        assertNotNull(assignmentGetListRes.body!!.data)

        assertEquals(count + randomCount, assignmentGetListRes.body!!.data!!.assignments.size.toLong())

        var i = 0

        assignmentGetListRes.body!!.data!!.assignments.subList(
            assignmentGetListRes.body!!.data!!.assignments.lastIndex - randomCount + 1,
            assignmentGetListRes.body!!.data!!.assignments.lastIndex
        ).forEach{
            assertEquals(assignmentList[i].id, it.id)
            assertEquals(assignmentList[i].title, it.title)
            assertEquals(assignmentList[i].description, it.description)
            assertEquals(assignmentList[i].score, it.score)
            assertEquals(assignmentList[i].startDate, it.startDate)
            assertEquals(assignmentList[i].endDate, it.endDate)

            i++
        }
    }

    @Test
    fun testGetEmptyList(){
        //given
        val allAssignmentList = assignmentRepository.findAll()
        assignmentRepository.deleteAll()

        //when
        val assignmentGetListRes = assignmentService.getList()


        //then

        assertEquals(HttpStatus.OK, assignmentGetListRes.statusCode)

        assertNotNull(assignmentGetListRes.body)
        assertNotNull(assignmentGetListRes.body!!.data)
        assertEquals(0, assignmentGetListRes.body!!.data!!.assignments.size)


        assignmentRepository.saveAll(allAssignmentList)
    }
}