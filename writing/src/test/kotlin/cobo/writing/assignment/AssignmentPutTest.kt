package cobo.writing.assignment

import cobo.writing.data.entity.Assignment
import cobo.writing.repository.AssignmentRepository
import cobo.writing.service.AssignmentService
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AssignmentPutTest @Autowired constructor(
    private val assignmentRepository : AssignmentRepository,
    private val assignmentService: AssignmentService
){

    private val assignmentList = mutableListOf<Assignment>()

    @AfterEach
    fun clear(){
        assignmentRepository.deleteAll(assignmentList)
        assignmentList.clear()
    }


    //그냥
    //중간
    //없는거
}