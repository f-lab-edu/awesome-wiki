import com.github.javafaker.Faker
import com.github.javafaker.service.FakeValuesService
import com.github.javafaker.service.RandomService
import kr.flab.wiki.core.login.business.UserLoginService
import kr.flab.wiki.core.login.business.UserLoginServiceImpl
import kr.flab.wiki.core.login.persistence.User
import kr.flab.wiki.core.login.persistence.UserImpl
import kr.flab.wiki.core.login.respository.SessionRepository
import kr.flab.wiki.core.login.respository.UserLoginRepository
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*

//테스트함수 표기에 한글 사용
@Suppress("NonAsciiCharacters")
class UserLoginServiceTest {
    lateinit var mockLoginRepository: UserLoginRepository
    lateinit var mockSessionRepository: SessionRepository
    lateinit var sut: UserLoginService
    lateinit var user: User

    @BeforeEach
    fun setup() {
        mockLoginRepository = mock(UserLoginRepository::class.java)
        mockSessionRepository = mock(SessionRepository::class.java)
        sut = UserLoginServiceImpl(mockLoginRepository, mockSessionRepository)
        user = createRandomUser()
    }

    fun createRandomUser(): User {
        val fakeValueService = FakeValuesService(Locale("en-GB"), RandomService())
        return UserImpl(
            fakeValueService.bothify("????##@gmail.com"),
            Faker.instance().name().toString()
        )
    }

    @Test
    fun `랜덤 유저가`() {
        @Nested
        class `로그인 시도할 경우` {
            @Test
            fun `미등록 상태에서 로그인 시도시 널 리턴`() {
                assertTrue(sut.login(user) == null)
            }
        }
    }

    @Test
    fun `랜덤 유저가 미등록 상태에서 로그인 시도할 경우 널 리턴`() {
        assertTrue(sut.login(user) == null)
    }

    @Test
    fun `랜덤 유저가 등록 후 로그인 시도할 경우 세션정보 저장 후 계정정보 리턴`() {
        `when`(mockLoginRepository.save(user)).thenReturn(user)
        //login 실행시 생성된 세션 uid를 리턴해줘야 함
        `when`(mockSessionRepository.getKey(user.email)).thenReturn("nonNull")
        assertTrue(sut.register(user) == user)
        //로그인하면서 랜덤 세션 uid생성
        assertTrue(sut.login(user) == user)
        //세션ID존재
        assertTrue(mockSessionRepository.getKey(user.email) != null)

    }

    @Test
    fun `중복 가입 시도시 에러 리턴`() {
        `when`(mockLoginRepository.save(user)).thenReturn(user).thenReturn(null)
        assertTrue(sut.register(user) == user)
        assertTrue(sut.register(user) == null)
    }
}