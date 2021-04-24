package kr.flab.wiki.core.login.utils;

import com.github.javafaker.Faker;
import kr.flab.wiki.core.login.persistence.User;
import kr.flab.wiki.core.login.persistence.UserEntity;

public class UserTestUtils {
    public static User createRandomUserEntity() {
        Faker faker = StringRandomUtils.getFaker();
        return new UserEntity(
                faker.internet().emailAddress(),
                faker.internet().password()
        );
    }
}
