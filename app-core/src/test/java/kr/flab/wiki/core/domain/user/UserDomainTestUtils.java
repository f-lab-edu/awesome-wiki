package kr.flab.wiki.core.domain.user;

import kr.flab.wiki.core.domain.StringRandomUtils;
import kr.flab.wiki.core.domain.user.persistence.UserEntity;

/**
 * javafaker 라이브러리 써서 이름 등 진짜로 random 문자열 쓰도록 고치기.
 */
public class UserDomainTestUtils {
    public static UserEntity createRandomUserEntity() {
        UserEntity user = new UserEntity();
        user.setName(StringRandomUtils.getFaker().name().fullName());
        return user;
    }

}
