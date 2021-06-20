package kr.flab.wiki.core.testcase.user

import com.github.javafaker.Faker
import kr.flab.wiki.TAG_TEST_UNIT
import kr.flab.wiki.core.common.exception.user.UserEmailAlreadyExistException
import kr.flab.wiki.core.common.exception.user.UserNameAlreadyExistException
import kr.flab.wiki.core.common.exception.user.WrongUserEmailException
import kr.flab.wiki.core.common.exception.user.WrongUserNameException
import kr.flab.wiki.core.domain.document.repository.UserHistoryRepository
import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.domain.user.UserRegistrationPolicy
import kr.flab.wiki.core.domain.user.UserService
import kr.flab.wiki.core.domain.user.persistence.UserEntity
import kr.flab.wiki.core.domain.user.repository.UserRepository
import kr.flab.wiki.core.testlib.document.Documents
import kr.flab.wiki.core.testlib.user.Users
import kr.flab.wiki.lib.time.utcNow
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

@Tag(TAG_TEST_UNIT)
@DisplayName("UserService 의 동작 시나리오를 확인한다.")
@Suppress("ClassName", "NonAsciiCharacters") // 테스트 표현을 위한 한글 사용
class UserServiceTest {
    private val faker = Faker.instance()

    @Mock
    private lateinit var userRepo: UserRepository
    @Mock
    private lateinit var historyRepository: UserHistoryRepository

    private lateinit var sut: UserService
    private val userName = faker.lorem().characters(
        UserRegistrationPolicy.DEFAULT_MINIMUM_USER_NAME_LENGTH + 1,
        UserRegistrationPolicy.DEFAULT_MAXIMUM_USER_NAME_LENGTH - 1
    )
    private val email = faker.internet().emailAddress()

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        this.sut = UserService.newInstance(userRepo, userHistoryRepository = historyRepository)
    }

    @Nested
    inner class `사용자 이름이` {
        // given:
        private val email = faker.internet().emailAddress()

        @Test
        fun `너무 짧으면 가입에 실패한다`() {
            // given:
            val userName = faker.lorem().characters(0, UserRegistrationPolicy.DEFAULT_MINIMUM_USER_NAME_LENGTH)

            // expect:
            assertThrows(WrongUserNameException::class.java) { sut.registerUser(userName, email) }
        }

        @Test
        fun `너무 길면 가입에 실패한다`() {
            // given:
            val userName = faker.lorem().characters(
                UserRegistrationPolicy.DEFAULT_MAXIMUM_USER_NAME_LENGTH + 1,
            )

            // expect:
            assertThrows(WrongUserNameException::class.java) { sut.registerUser(userName, email) }
        }
    }

    @Nested
    inner class `이메일 주소가` {
        // given:
        private val userName = faker.lorem().characters(
            UserRegistrationPolicy.DEFAULT_MINIMUM_USER_NAME_LENGTH + 1,
            UserRegistrationPolicy.DEFAULT_MAXIMUM_USER_NAME_LENGTH - 1
        )

        @Test
        fun `너무 짧으면 가입에 실패한다`() {
            // given:
            val email = faker.lorem().characters(0, UserRegistrationPolicy.DEFAULT_MINIMUM_EMAIL_LENGTH)

            // expect:
            assertThrows(WrongUserEmailException::class.java) { sut.registerUser(userName, email) }
        }

        @Test
        fun `너무 길면 가입에 실패한다`() {
            // given:
            val email = faker.lorem().characters(0, UserRegistrationPolicy.DEFAULT_MINIMUM_EMAIL_LENGTH)

            // expect:
            assertThrows(WrongUserEmailException::class.java) { sut.registerUser(userName, email) }
        }
    }

    @Nested
    inner class `이미 사용한` {
        // given:
        private val userName = faker.lorem().characters(
            UserRegistrationPolicy.DEFAULT_MINIMUM_USER_NAME_LENGTH + 1,
            UserRegistrationPolicy.DEFAULT_MAXIMUM_USER_NAME_LENGTH - 1
        )
        private val email = faker.internet().emailAddress()

        // and:
        private val alreadyExistingUser = Users.randomUser()

        @Test
        fun `userName 으로 가입 시도시 실패한다`() {
            // when:
            `when`(userRepo.findByUserName(userName)).thenReturn(alreadyExistingUser)

            // expect:
            assertThrows(UserNameAlreadyExistException::class.java) { sut.registerUser(userName, email) }
        }

        @Test
        fun `email 로 가입 시도시 실패한다`() {
            // when:
            `when`(userRepo.findByEmail(email)).thenReturn(alreadyExistingUser)

            // expect:
            assertThrows(UserEmailAlreadyExistException::class.java) { sut.registerUser(userName, email) }
        }
    }


    @Nested
    inner class `사용자 등록 시` {
        lateinit var user: User

        @BeforeEach
        fun setUp() {
            `when`(userRepo.save(any())).thenAnswer { it.arguments[0] as UserEntity }
            user = sut.registerUser(userName, email)
        }
        @Test
        fun `올바른 파라미터를 입력하면 가입에 성공한다`() {
            // then:
            val createdUser = sut.registerUser(userName, email)

            // expect:
            assertThat(createdUser.userName, `is`(userName))
            assertThat(createdUser.emailAddress, `is`(email))
        }

        @Test
        fun `문서 기여 리스트 조회시 생성내역 리턴`() {
            val now = utcNow()
            val yesterdayDoc = Documents.randomDocument(creator=user, createdAt = now.minusDays(1))
            val todayDoc = Documents.randomDocument(creator=user, createdAt = now)
            val futureDoc = Documents.randomDocument(creator=user, createdAt = now.plusDays(1))
            val mockResult = listOf(yesterdayDoc, todayDoc, futureDoc)
            val range = now.minusDays(1)..now
            `when`(historyRepository.getHistory(user, range)).thenReturn(mockResult.filter { e -> e.createdAt in range })
            val history = sut.findUserHistory(user, range);

            assertThat(history[0], `is`(mockResult[0]))
            assertThat(history[1], `is`(mockResult[1]))
        }
    }
}
