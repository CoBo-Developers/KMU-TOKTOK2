package cobo.auth.user

import cobo.auth.data.entity.User
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import cobo.auth.repository.UserRepository
import cobo.auth.service.UserService
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserGetListTest(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserRepository,
) {

    private val user = User(
        id = null,
        studentId = "0002",
        role = RoleEnum.STUDENT,
        registerState = RegisterStateEnum.ACTIVE
    )

    private val addUser = User(
        id = null,
        studentId = "0001",
        role = RoleEnum.STUDENT,
        registerState = RegisterStateEnum.ACTIVE
    )

    private val deleteUser = User(
        id = null,
        studentId = "0000",
        role = RoleEnum.STUDENT,
        registerState = RegisterStateEnum.ACTIVE
    )

    @BeforeEach
    fun init(){
        userRepository.saveAll(listOf(user))
    }

    @AfterEach
    fun clear(){
        userRepository.deleteAll(listOf(user, addUser, deleteUser))
    }

    @Test
    fun getUserListWithAddUser(){
        //given
        val userCount = userRepository.count()

        //when
        val userGetList1 = userService.getList(0, userCount.toInt())
        userRepository.save(addUser)
        val userGetList2 = userService.getList(0, userCount.toInt() + 1)
        println(userGetList1)

        //then
        assert(userGetList1.body!!.data!!.totalElements + 1 == userGetList2.body!!.data!!.totalElements)
        println(userGetList1.body!!.data!!.users)
        assert(userGetList1.body!!.data!!.users.first().studentId == user.studentId)
        assert(userGetList1.body!!.data!!.users.first().role == user.role.toString())
        assert(userGetList1.body!!.data!!.users.first().registerState == user.registerState.toString())

        assert(userGetList2.body!!.data!!.users.first().studentId == addUser.studentId)
        assert(userGetList2.body!!.data!!.users.first().role == addUser.role.toString())
        assert(userGetList2.body!!.data!!.users.first().registerState == addUser.registerState.toString())
    }

    @Test
    fun getUserListWithDeleteUser(){
        //given
        userRepository.save(deleteUser)
        val userCount = userRepository.count()

        //when
        val userGetList1 = userService.getList(0, userCount.toInt())
        userRepository.delete(deleteUser)
        val userGetList2 = userService.getList(0, userCount.toInt())

        //then
        assert(userGetList1.body!!.data!!.totalElements == userGetList2.body!!.data!!.totalElements + 1)
        assert(userGetList1.body!!.data!!.users.first().studentId == deleteUser.studentId)
        assert(userGetList1.body!!.data!!.users.first().role == deleteUser.role.toString())
        assert(userGetList1.body!!.data!!.users.first().registerState == deleteUser.registerState.toString())

        assert(userGetList2.body!!.data!!.users.first().studentId == user.studentId)
        assert(userGetList2.body!!.data!!.users.first().role == user.role.toString())
        assert(userGetList2.body!!.data!!.users.first().registerState == user.registerState.toString())
    }

}