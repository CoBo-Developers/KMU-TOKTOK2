package cobo.auth.user

import cobo.auth.data.dto.user.PutUserReq
import cobo.auth.data.entity.User
import cobo.auth.data.enums.RegisterStateEnum
import cobo.auth.data.enums.RoleEnum
import cobo.auth.repository.UserRepository
import cobo.auth.service.UserService
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.scheduling.annotation.Async
import org.springframework.test.annotation.DirtiesContext
import org.springframework.transaction.annotation.Transactional
import kotlin.math.ceil
import kotlin.test.Test

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserPutTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService
){

    private var user = User(
        id = null,
        studentId = (ceil(Math.random() * 1000000) % 1000000).toInt().toString(),
        role = RoleEnum.STUDENT,
        registerState = RegisterStateEnum.ACTIVE,
    )



    @BeforeEach
    fun beforeEach(){
        user.id = null
        user.studentId = (ceil(Math.random() * 1000000) % 1000000).toInt().toString()
        user.role = RoleEnum.STUDENT
        user.registerState = RegisterStateEnum.INACTIVE
        userRepository.save(user)
    }

    @AfterEach
    fun afterEach(){
        userRepository.delete(user)
    }

    @Test
    fun changeRole(){
        for(startRole in RoleEnum.entries){
            for(endRole in RoleEnum.entries){
                //given
                user.role = startRole
                userRepository.save(user)

                //when
                userService.put(PutUserReq(
                    studentId = user.studentId!!,
                    role = endRole.value,
                    registerState = user.registerState.value,
                    newStudentId = user.studentId!!
                ))

                //then
                val newUser = userRepository.findById(user.id!!).orElseThrow()

                assert(newUser.studentId == user.studentId)
                assert(newUser.role == endRole)
                assert(newUser.registerState == user.registerState)
                assert(newUser.id == user.id)
            }
        }
    }

    @Test
    fun changeRegisterState(){
        for(startRegisterState in RegisterStateEnum.entries){
            for(endRegisterState in RegisterStateEnum.entries){
                //given
                user.registerState = startRegisterState
                userRepository.save(user)

                //when
                userService.put(PutUserReq(
                    studentId = user.studentId!!,
                    role = user.role.value,
                    registerState = endRegisterState.value,
                    newStudentId = user.studentId!!
                ))

                //then
                val newUser = userRepository.findById(user.id!!).orElseThrow()

                assert(newUser.studentId == user.studentId)
                assert(newUser.role == user.role)
                assert(newUser.registerState == endRegisterState)
                assert(newUser.id == user.id)
            }
        }
    }

    @Test
    fun changeStudentId(){
        //given
        val studentId = (ceil(Math.random() * 1000000) % 1000000).toInt().toString()

        //when
        userService.put(PutUserReq(
            studentId = user.studentId!!,
            role = user.role.value,
            registerState = user.registerState.value,
            newStudentId = studentId
        ))

        //then
        val newUser = userRepository.findById(user.id!!).orElseThrow()

        assert(newUser.studentId == studentId)
        assert(newUser.registerState == user.registerState)
        assert(newUser.role == user.role)
    }

    @Test
    fun changeRoleAndRegisterState(){
        //given
        val role =  RoleEnum.from( (0..2).random().toShort())
        val registerState = RegisterStateEnum.from ( (0..1).random().toShort()) !!

        //when
        userService.put(PutUserReq(
            studentId = user.studentId!!,
            role = role.value,
            registerState = registerState.value,
            newStudentId = user.studentId!!
        ))

        //then
        val newUser = userRepository.findById(user.id!!).orElseThrow()

        assert(newUser.role == role)
        assert(newUser.registerState == registerState)
        assert(newUser.studentId == user.studentId)
        assert(newUser.id == user.id)
    }

    @Test
    fun changeRegisterStateAndStudentId(){
        //given
        val registerState = RegisterStateEnum.from ( (0..1).random().toShort()) !!
        val studentId = (ceil(Math.random() * 1000000) % 1000000).toInt().toString()

        //when
        userService.put(PutUserReq(
            studentId = user.studentId !!,
            role = user.role.value,
            registerState = registerState.value,
            newStudentId = studentId
        ))

        //then
        val newUser = userRepository.findById(user.id!!).orElseThrow()

        assert(newUser.role == user.role)
        assert(newUser.registerState == registerState)
        assert(newUser.studentId == studentId)
        assert(newUser.id == user.id)
    }

    @Test
    fun changeRoleAndStudentId(){
        //given
        val role =  RoleEnum.from( (0..2).random().toShort())
        val studentId = (ceil(Math.random() * 1000000) % 1000000).toInt().toString()

        //when
        userService.put(PutUserReq(
            studentId = user.studentId !!,
            role = role.value,
            registerState = user.registerState.value,
            newStudentId = studentId
        ))

        //then
        val newUser = userRepository.findById(user.id!!).orElseThrow()

        assert(newUser.role == role)
        assert(newUser.registerState == user.registerState)
        assert(newUser.studentId == studentId)
        assert(newUser.id == user.id)
    }

    @Test
    fun changeAll(){
        //given
        val role =  RoleEnum.from( (0..2).random().toShort())
        val registerState = RegisterStateEnum.from ( (0..1).random().toShort()) !!
        val studentId = (ceil(Math.random() * 1000000) % 1000000).toInt().toString()

        //when
        userService.put(PutUserReq(
            studentId = user.studentId !!,
            role = role.value,
            registerState = registerState.value,
            newStudentId = studentId
        ))

        //then
        val newUser = userRepository.findById(user.id!!).orElseThrow()

        assert(newUser.role == role)
        assert(newUser.registerState == registerState)
        assert(newUser.studentId == studentId)
        assert(newUser.id == user.id)
    }
}

