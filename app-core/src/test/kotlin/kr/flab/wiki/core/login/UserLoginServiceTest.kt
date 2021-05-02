package kr.flab.wiki.core.login

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
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

@SuppressWarnings("NonAsciiCharacters") // 클래스 명에 한글을 사용하기 위함.
@DisplayName("해당 테스트는 UserLoginService 메서드를 테스트하는 클래스이다.")
class UserLoginServiceTest {

    lateinit var mockUserLoginRepository : UserLoginRepository
    lateinit var mockSessionRepository : SessionRepository
    lateinit var sut : UserLoginService

    @BeforeEach
    private fun setup() {

        mockUserLoginRepository = mock(UserLoginRepositoryImpl::class.java)
        mockSessionRepository = mock(SessionRepositoryImpl::class.java)
        sut = UserLoginServiceImpl(mockUserLoginRepository, mockSessionRepository)

    }

    @Nested
    @DisplayName("로그인 하지 않은 사용자는")
    inner class GivenAnonymousUser {

        private val user : User = createRandomUserEntity()

        @Nested
        @DisplayName("이메일/비밀번호 입력 후 로그인 버튼을 클릭했을 때")
        inner class WhenClickLoginButton {

            @Nested
            @DisplayName("이메일/비밀번호가 일치하는 회원이 있으면")
            inner class AndEmailPasswordIsCorrect {

                @Test
                @DisplayName("세션에 유저 정보를 저장하고 로그인에 성공한다.")
                fun thenCreateSessionAndLoginSuccess() {

                    `when`(mockUserLoginRepository.findUserWithIdAndPassword(user.email, user.password)).thenReturn(user)
                    `when`(mockSessionRepository.setAttribute("userName", user.email)).thenReturn(user.email)

                    val loggedInUser : User? = sut.login(user)

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

                    `when`(mockUserLoginRepository.findUserWithIdAndPassword(user.email, user.password)).thenReturn(null)
                    `when`(mockSessionRepository.setAttribute("userName", user.email)).thenReturn(user.email)

                    val loginException : UserValidationException = assertThrows(UserValidationException::class.java) { sut.login(user) }

                    assertThat(loginException.message, `is`("There's No Matched Member!"))
                    verify(mockSessionRepository, times(0)).setAttribute("userName", user.email)

                }

            }

            @Nested
            @DisplayName("이메일 혹은 비밀번호가 비어있으면")
            inner class AndEmailOrPasswordIsBlank {

                private val emailBlank : User = createRandomUserEntity(UserType.BLANK_EMAIL)
                private val passwordBlank : User = createRandomUserEntity(UserType.BLANK_PASSWORD)

                @Test
                @DisplayName("로그인에 실패한다.")
                fun thenLoginFailed() {

                    var loginException : UserValidationException = assertThrows(UserValidationException::class.java) { sut.login(emailBlank) }

                    assertThat(loginException.message, `is`("Email is Empty!"))

                    loginException = assertThrows(UserValidationException::class.java) { sut.login(passwordBlank) }

                    assertThat(loginException.message, `is`("Password is Empty!"))

                }

            }

            @Nested
            @DisplayName("이메일 형식이 올바르지 않으면")
            inner class AndNotCorrectEmailType {

                private val notEmail : User = createRandomUserEntity(UserType.NOT_EMAIL)

                @Test
                @DisplayName("로그인에 실패한다.")
                fun thenLoginFailed() {

                    val loginException : UserValidationException = assertThrows(UserValidationException::class.java) { sut.login(notEmail) }

                    assertThat(loginException.message, `is`("Not Email Type!"))

                }

            }


        }

    }

}