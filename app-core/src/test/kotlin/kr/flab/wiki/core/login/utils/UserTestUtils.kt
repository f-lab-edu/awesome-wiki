package kr.flab.wiki.core.login

import com.github.javafaker.Faker
import kr.flab.wiki.core.login.persistence.UserEntity
import java.util.*

object UserTestUtils {
    val faker : Faker = Faker(Locale.ENGLISH)
}

fun createRandomUserEntity() = UserEntity().apply {
    email = UserTestUtils.faker.internet().emailAddress()
    password = UserTestUtils.faker.internet().password()
}

fun createRandomUserEntity(type : UserType) =
    when(type){
        UserType.BLANK_EMAIL -> UserEntity().apply {
            email = ""
            password = UserTestUtils.faker.internet().password()
        }
        UserType.BLANK_PASSWORD -> UserEntity().apply {
            email = UserTestUtils.faker.internet().emailAddress()
            password = ""
        }
        UserType.NOT_EMAIL -> UserEntity().apply {
            email = UserTestUtils.faker.animal().name()
            password = ""
        }
    }

enum class UserType() {
    BLANK_EMAIL,
    BLANK_PASSWORD,
    NOT_EMAIL
}