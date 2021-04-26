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

    public static User createRandomUserEntity(UserType userType) {
        Faker faker = StringRandomUtils.getFaker();
        if(userType == UserType.BLANK_EMAIL){
            return new UserEntity("", faker.internet().password());
        }
        if(userType == UserType.BLANK_PASSWORD){
            return new UserEntity(faker.internet().emailAddress(), "");
        }
        if(userType == UserType.NOT_EMAIL){
            return new UserEntity(faker.animal().name(), faker.internet().password());
        }
        return new UserEntity("", "");
    }

    public enum UserType {
        BLANK_EMAIL,
        BLANK_PASSWORD,
        NOT_EMAIL
    }


}
