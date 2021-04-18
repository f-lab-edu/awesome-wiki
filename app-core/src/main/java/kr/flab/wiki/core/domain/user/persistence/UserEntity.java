package kr.flab.wiki.core.domain.user.persistence;

import kr.flab.wiki.core.domain.user.User;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * 실제로 Persistent store 에 저장되는 데이터를 모델링 한 구현체
 */
public class UserEntity implements User {
    private String name;
    private LocalDateTime lastActiveAt;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LocalDateTime getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(LocalDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }
}
