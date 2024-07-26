package cobo.auth.user

import cobo.auth.data.entity.User
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import cobo.auth.repository.UserRepository
import cobo.auth.service.UserService
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserGetListTest(
    @Autowired private val userService: UserService
) {

    @Autowired
    private lateinit var userRepository: UserRepository

    companion object {
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

        @JvmStatic
        @BeforeAll
        internal fun beforeAll(
            @Autowired userRepository: UserRepository,
        ) {
            userRepository.saveAll(listOf(user, deleteUser))
        }

        @JvmStatic
        @AfterAll
        internal fun afterAll(
            @Autowired userRepository: UserRepository,
        ) {
            userRepository.deleteAll(listOf(user, addUser))
        }
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