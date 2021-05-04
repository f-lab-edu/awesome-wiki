package kr.flab.wiki.core.login.business;

import kr.flab.wiki.core.login.persistence.User;

public interface UserLoginService {
    User login(User user);
}
