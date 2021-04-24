package kr.flab.wiki.core.login.repository;

import kr.flab.wiki.core.login.persistence.User;

public interface UserLoginRepository {
    boolean findByIdWithPassword(User user);
}
