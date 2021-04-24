package kr.flab.wiki.core.login;

import kr.flab.wiki.core.login.business.UserLoginService;
import kr.flab.wiki.core.login.business.UserLoginServiceImpl;
import kr.flab.wiki.core.login.persistence.User;
import kr.flab.wiki.core.login.repository.SessionRepository;
import kr.flab.wiki.core.login.repository.SessionRepositoryImpl;
import kr.flab.wiki.core.login.repository.UserLoginRepository;
import kr.flab.wiki.core.login.repository.UserLoginRepositoryImpl;
import kr.flab.wiki.core.login.utils.UserTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("UserLoginServiceTest")
public class UserLoginServiceTest {
    /**
     * 로그인 기능 테스트
     *
     * given : 로그인 하지 않은 사용자는
     * when : 이메일/비밀번호 입력 후 로그인 버튼을 클릭했을 때
     * and : 이메일/비밀번호가 일치하는 회원이 있으면
     * expected/then : 세션에 유저 정보를 저장하고
     * and : 로그인에 성공한다.
     *
     * given : 로그인 하지 않은 사용자는
     * when : 이메일/비밀번호 입력 후 로그인 버튼을 클릭했을 때
     * and : 이메일이 이메일 형식이 아니면
     * expected/then : 로그인에 실패한다.
     *
     * given : 로그인 하지 않은 사용자는
     * when : 이메일/비밀번호 입력 후 로그인 버튼을 클릭했을 때
     * and : 이메일 혹은 비밀번호가 비어있으면
     * expected/then : 로그인에 실패한다.
     *
     * given : 로그인 하지 않은 사용자는
     * when : 이메일/비밀번호 입력 후 로그인 버튼을 클릭했을 때
     * and : 이메일/비밀번호가 일치하는 회원이 없으면
     * expected/then : 로그인에 실패한다.
     *
     *
     *
     */

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
            class And_Email_Password_Correct {

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





        }

    }


//    @Nested
//    @DisplayName("login 메서드는")
//    class Describe_login {
//
//        @Nested
//        @DisplayName("username과 password가 일치하면")
//        class Context_with_username_password_correct {
//
//            @Test
//            @DisplayName("로그인에 성공한다.")
//            void it_login_success() {
//
//            }
//        }
//    }

}
