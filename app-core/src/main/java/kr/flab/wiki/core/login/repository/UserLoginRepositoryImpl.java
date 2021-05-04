package kr.flab.wiki.core.login.repository;

import kr.flab.wiki.core.login.persistence.User;

import java.util.HashMap;

public class UserLoginRepositoryImpl implements UserLoginRepository{
    HashMap<String, String> database = new HashMap<>();
    @Override
    public boolean findByIdWithPassword(User user) {
        String password = database.get(user.getEmail());
        if(password != null){
            return isPasswordEquals(password, user.getPassword());
        }
        return false;
    }

    private boolean isPasswordEquals(String origin, String attempt) {
        return origin.equals(attempt);
    }
}
