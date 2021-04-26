package kr.flab.wiki.core.login.business;

import kr.flab.wiki.core.login.LoginUtils;
import kr.flab.wiki.core.login.exception.LoginException;
import kr.flab.wiki.core.login.persistence.User;
import kr.flab.wiki.core.login.repository.SessionRepository;
import kr.flab.wiki.core.login.repository.UserLoginRepository;

public class UserLoginServiceImpl implements UserLoginService {

    private final UserLoginRepository userLoginRepository;
    private final SessionRepository sessionRepository;

    public UserLoginServiceImpl(
            UserLoginRepository userLoginRepository,
            SessionRepository sessionRepository
    ){
        this.userLoginRepository = userLoginRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public User login(User user) {

        String email = user.getEmail();
        String password = user.getPassword();

        //이메일이 비어있을 경우
        if(email.isEmpty()){
            throw new LoginException("Email is Empty!");
        }

        //이메일 형식이 아닐 경우
        if(!LoginUtils.isValidEmail(email)){
            throw new LoginException("Not Email Type!");
        }

        //비밀번호가 비어있을 경우
        if(password.isEmpty()){
            throw new LoginException("Password is Empty!");
        }

        boolean isMatched = userLoginRepository.findByIdWithPassword(user);

        //매치되는 회원이 없을 경우
        if(!isMatched){
            throw new LoginException("There's No Matched Member!");
        }

        //세션 저장
        if(sessionRepository.setAttribute("userName", email) == null){
            throw new LoginException("Session creation failed!");
        }
        return isMatched ? user : null;
    }
}
