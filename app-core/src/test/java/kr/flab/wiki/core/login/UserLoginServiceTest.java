package kr.flab.wiki.core.login;

import kr.flab.wiki.core.login.business.UserLoginService;
import kr.flab.wiki.core.login.business.UserLoginServiceImpl;
import kr.flab.wiki.core.login.exception.LoginException;
import kr.flab.wiki.core.login.persistence.User;
import kr.flab.wiki.core.login.persistence.UserEntity;
import kr.flab.wiki.core.login.repository.SessionRepository;
import kr.flab.wiki.core.login.repository.SessionRepositoryImpl;
import kr.flab.wiki.core.login.repository.UserLoginRepository;
import kr.flab.wiki.core.login.repository.UserLoginRepositoryImpl;
import kr.flab.wiki.core.login.utils.UserTestUtils;
import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("UserLoginServiceTest")
public class UserLoginServiceTest {

    private UserLoginRepository mockUserLoginRepository;
    private SessionRepository mockSessionRepository;
    private UserLoginService sut;

    @BeforeEach
    private void setUp(){
        mockUserLoginRepository = mock(UserLoginRepositoryImpl.class);
        mockSessionRepository = mock(SessionRepositoryImpl.class);

        sut = new UserLoginServiceImpl(mockUserLoginRepository, mockSessionRepository);

    }

    @Nested
    @DisplayName("로그인 하지 않은 사용자는")
    class Given_Anonymous_User {

        // random user creation
        User user = UserTestUtils.createRandomUserEntity();

        @Nested
        @DisplayName("이메일/비밀번호 입력 후 로그인 버튼을 클릭했을 때")
        class When_Click_Login_Button {

            @Nested
            @DisplayName("이메일/비밀번호가 일치하는 회원이 있으면")
            class And_Email_Password_is_Correct {

                //여기선 해당 이메일/비밀번호가 일치하는 정보가 데이터베이스에 있다고 가정한다.

                @Test
                @DisplayName("세션에 유저 정보를 저장하고 로그인에 성공한다.")
                void Then_Create_Session_And_Login_Success() {

                    //mock 설정
                    //무조건 일치한다고 가정
                    when(mockUserLoginRepository.findByIdWithPassword(user)).thenReturn(true);
                    //세션 생성 시 무조건 user 반환
                    when(mockSessionRepository.setAttribute("userName", user.getEmail())).thenReturn(user.getEmail());


                    User loggedInUser = sut.login(user);

                    //session에 저장여부 확인
                    verify(mockSessionRepository, times(1)).setAttribute("userName", user.getEmail());
                    //현재 이메일과 로그인 성공한 이메일이 같은지 검증
                    assertThat(user.getEmail(), is(loggedInUser.getEmail()));

                }

            }

            @Nested
            @DisplayName("이메일/비밀번호가 일치하는 회원이 없으면")
            class And_Email_Password_is_Not_Correct {


                @Test
                @DisplayName("로그인에 실패한다.")
                void Then_Login_Failed() {

                    when(mockUserLoginRepository.findByIdWithPassword(user)).thenReturn(false);

                    when(mockSessionRepository.setAttribute("userName", user.getEmail())).thenReturn(user.getEmail());


                    //로그인 실패 exception 발생
                    LoginException loginException = assertThrows(LoginException.class, () -> sut.login(user));

                    assertThat(loginException.getMessage(), is("There's No Matched Member!"));
                    verify(mockSessionRepository, times(0)).setAttribute("userName", user.getEmail());

                }


            }

            @Nested
            @DisplayName("이메일 혹은 비밀번호가 비어있으면")
            class And_Email_Or_Password_is_Blank {

                User emailBlank = UserTestUtils.createRandomUserEntity(UserTestUtils.UserType.BLANK_EMAIL);
                User passwordBlank = UserTestUtils.createRandomUserEntity(UserTestUtils.UserType.BLANK_PASSWORD);

                @Test
                @DisplayName("로그인에 실패한다.")
                void Then_Login_Failed() {

                    LoginException loginException = assertThrows(LoginException.class, ()-> sut.login(emailBlank));

                    assertThat(loginException.getMessage(), is("Email is Empty!"));

                    loginException = assertThrows(LoginException.class, ()-> sut.login(passwordBlank));

                    assertThat(loginException.getMessage(), is("Password is Empty!"));

                }


            }

            @Nested
            @DisplayName("이메일 형식이 올바르지 않으면")
            class And_Not_Correct_Email_Type {

                @Test
                @DisplayName("로그인에 실패한다.")
                void Then_Login_Failed() {

                    User notEmail = UserTestUtils.createRandomUserEntity(UserTestUtils.UserType.NOT_EMAIL);

                    LoginException loginException = assertThrows(LoginException.class, ()-> sut.login(notEmail));

                    assertThat(loginException.getMessage(), is("Not Email Type!"));

                }

            }

        }

    }

}
