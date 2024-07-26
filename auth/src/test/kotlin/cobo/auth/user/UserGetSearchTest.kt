package cobo.auth.user

import cobo.auth.config.response.CoBoResponse
import cobo.auth.config.response.CoBoResponseStatus
import cobo.auth.data.dto.user.GetUserListRes
import cobo.auth.data.dto.user.GetUserRes
import cobo.auth.data.entity.User
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import cobo.auth.repository.UserRepository
import cobo.auth.service.UserService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest
class UserGetSearchTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService
) {

    private val user = User(
        id = null,
        studentId = "aaaa",
        role = RoleEnum.STUDENT,
        registerState = RegisterStateEnum.ACTIVE
    )

    private val addUser = User(
        id = null,
        studentId = "cccc",
        role = RoleEnum.STUDENT,
        registerState = RegisterStateEnum.ACTIVE
    )

    @BeforeEach
    fun init() {
        user.id = null

        userRepository.save(user)
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll(listOf(user, addUser))
    }

    @Test
    fun getSearchValidUser(){
        //given

        //when
        val getSearchResponse = userService.getSearch(studentId = "a", pageSize = 10, page = 0)

        //then
        assert(getSearchResponse.statusCode == HttpStatus.OK)
        assert(getSearchResponse.body != null)
        assert(getSearchResponse.body!!.code == CoBoResponseStatus.SUCCESS.code)
        assert(getSearchResponse.body!!.data is GetUserListRes)
        assert(getSearchResponse.body!!.data?.totalElements == 1L)
        assert(getSearchResponse.body!!.data!!.users[0] == GetUserRes(user))
    }

    @Test
    fun getSearchInvalidUser(){
        //given

        //when
        val getSearchResponse = userService.getSearch(studentId = "b", pageSize = 10, page = 0)

        //then
        assert(getSearchResponse.statusCode == HttpStatus.OK)
        assert(getSearchResponse.body != null)
        assert(getSearchResponse.body!!.code == CoBoResponseStatus.SUCCESS.code)
        assert(getSearchResponse.body!!.data is GetUserListRes)
        assert(getSearchResponse.body!!.data?.totalElements == 0L)
        println(getSearchResponse.body!!.data!!.users)
        assert(getSearchResponse.body!!.data!!.users.isEmpty())
    }

    @Test
    fun getSearchAddUser(){
        //given
        val getSearchResponse1 = userService.getSearch(studentId = "c", pageSize = 10, page = 0)
        userRepository.save(addUser)

        //when
        val getSearchResponse2 = userService.getSearch(studentId = "c", pageSize = 10, page = 0)

        //then
        assert(getSearchResponse1.statusCode == HttpStatus.OK)
        assert(getSearchResponse2.statusCode == HttpStatus.OK)

        assert(getSearchResponse2.body != null)
        assert(getSearchResponse1.body!!.data!!.totalElements + 1 == getSearchResponse2.body!!.data!!.totalElements)
    }

}