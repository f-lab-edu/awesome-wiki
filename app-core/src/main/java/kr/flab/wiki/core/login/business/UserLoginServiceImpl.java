package kr.flab.wiki.core.login.business;

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

        //세션 저장
        if(sessionRepository.setAttribute("userName", user.getEmail()) == null){
            throw new LoginException("Session creation failed!");
        }
        return userLoginRepository.findByIdWithPassword(user) ? user : null;
    }
}
