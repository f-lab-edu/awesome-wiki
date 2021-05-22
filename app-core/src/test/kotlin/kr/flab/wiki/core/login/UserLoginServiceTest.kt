package kr.flab.wiki.core.login

import kr.flab.wiki.TAG_TEST_UNIT
import kr.flab.wiki.core.login.business.UserLoginService
import kr.flab.wiki.core.login.business.UserLoginServiceImpl
import kr.flab.wiki.core.login.exception.UserValidationException
import kr.flab.wiki.core.login.persistence.User
import kr.flab.wiki.core.login.repository.SessionRepository
import kr.flab.wiki.core.login.repository.SessionRepositoryImpl
import kr.flab.wiki.core.login.repository.UserLoginRepository
import kr.flab.wiki.core.login.repository.UserLoginRepositoryImpl
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito.*

@Tag(TAG_TEST_UNIT)
@DisplayName("UserLoginService 의 동작 시나리오를 확인한다.")
class UserLoginServiceTest {
    private lateinit var mockUserLoginRepository: UserLoginRepository
    private lateinit var mockSessionRepository: SessionRepository
    private lateinit var sut: UserLoginService

    @BeforeEach
    private fun setup() {
        mockUserLoginRepository = mock(UserLoginRepositoryImpl::class.java)
        mockSessionRepository = mock(SessionRepositoryImpl::class.java)
        sut = UserLoginServiceImpl(mockUserLoginRepository, mockSessionRepository)
    }

    @Nested
    @DisplayName("로그인 하지 않은 사용자는")
    inner class GivenAnonymousUser {
        // given
        private val user: User = createRandomUserEntity()

        @Nested
        @DisplayName("이메일/비밀번호 입력 후 로그인 버튼을 클릭했을 때")
        inner class WhenClickLoginButton {

            @Nested
            @DisplayName("이메일/비밀번호가 일치하는 회원이 있으면")
            inner class AndEmailPasswordIsCorrect {

                @Test
                @DisplayName("세션에 유저 정보를 저장하고 로그인에 성공한다.")
                fun thenCreateSessionAndLoginSuccess() {
                    // when
                    `when`(
                        mockUserLoginRepository.findUserWithIdAndPassword(
                            user.email,
                            user.password
                        )
                    ).thenReturn(user)
                    `when`(mockSessionRepository.setAttribute("userName", user.email)).thenReturn(user.email)

                    // then
                    val loggedInUser: User? = sut.login(user)

                    // expect
                    verify(mockSessionRepository, times(1)).setAttribute("userName", user.email)
                    assertThat(user.email, `is`(loggedInUser?.email))
                }
            }

            @Nested
            @DisplayName("이메일/비밀번호가 일치하는 회원이 없으면")
            inner class AndEmailPasswordIsNotCorrect {

                @Test
                @DisplayName("로그인에 실패한다.")
                fun thenLoginFailed() {
                    // when
                    `when`(
                        mockUserLoginRepository.findUserWithIdAndPassword(
                            user.email,
                            user.password
                        )
                    ).thenReturn(null)
                    `when`(mockSessionRepository.setAttribute("userName", user.email)).thenReturn(user.email)

                    // then
                    val loginException: UserValidationException =
                        assertThrows(UserValidationException::class.java) { sut.login(user) }

                    // expect
                    assertThat(loginException.message, `is`("There's No Matched Member!"))
                    verify(mockSessionRepository, times(0)).setAttribute("userName", user.email)
                }
            }
        }
    }
}
