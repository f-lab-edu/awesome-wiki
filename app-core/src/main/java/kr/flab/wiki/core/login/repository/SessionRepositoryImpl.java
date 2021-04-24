package kr.flab.wiki.core.login.repository;

public class SessionRepositoryImpl implements SessionRepository{
    @Override
    public String setAttribute(String key, String username) {
        return username;
    }
}
