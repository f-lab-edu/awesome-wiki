package kr.flab.wiki.core.login.repository;

public interface SessionRepository {
    String setAttribute(String key, String username);
}
