package kr.flab.wiki.core.testcase.user

import com.github.javafaker.Faker
import kr.flab.wiki.TAG_TEST_UNIT
import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.domain.user.usecases.UserLoginUseCase
import kr.flab.wiki.core.domain.user.repository.UserRepository
import kr.flab.wiki.core.testlib.user.Users
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@Tag(TAG_TEST_UNIT)
@DisplayName("UserLoginUseCase 의 로그인 시나리오를 확인한다.")
@Suppress("ClassName", "NonAsciiCharacters") // 테스트 표현을 위한 한글 사용
class UserLoginUseCaseTest {

    private val faker = Faker.instance()

    @Mock
    private lateinit var userRepo: UserRepository

    private lateinit var sut: UserLoginUseCase

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        this.sut = UserLoginUseCase.newInstance(userRepo)
    }

    /**
     * 현재 Usecase에는 로그인 시 이메일, 비밀번호 검증에 대한 비즈니스 룰만 작성.
     */

    @Nested
    inner class 사용자는 {

        @Nested
        inner class `이메일과 비밀번호로 로그인시도를 했을 때` {
            // given :
            val email: String = faker.internet().emailAddress()
            val password: String = faker.internet().password()

            @Nested
            inner class 유효하면 {

                @Test
                fun `User 객체를 반환한다`() {
                    // when :
                    `when`(userRepo.findUserByEmailAndPassword(email, password)).thenReturn(Users.randomUser(emailAddress = email))

                    // then :
                    val user: User? = sut.login(email, password)

                    // expect :
                    assertThat(user?.emailAddress, `is`(email))

                }

            }

            @Nested
            inner class `유효하지 않으면` {

                @Test
                fun `null 을 반환한다`() {

                    // when :
                    `when`(userRepo.findUserByEmailAndPassword(email, password)).thenReturn(null)

                    // then :
                    val user: User? = sut.login(email, password)

                    // expect :
                    assertThat(user, `is`(nullValue()))

                }

            }

        }

    }

}
