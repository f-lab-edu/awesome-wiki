package kr.flab.wiki.core.login

import com.github.javafaker.Faker
import kr.flab.wiki.core.login.persistence.UserEntity
import kr.flab.wiki.core.post.persistence.PostEntity
import kr.flab.wiki.core.post.persistence.User
import java.time.LocalDateTime
import java.util.*

object TestUtils {
    val faker: Faker = Faker(Locale.ENGLISH)
}

fun createRandomUserEntity() = UserEntity().apply {
    email = TestUtils.faker.internet().emailAddress()
    password = TestUtils.faker.internet().password()
}

fun createRandomUserEntity(type: UserType) =
    when (type) {
        UserType.BLANK_EMAIL -> UserEntity().apply {
            email = ""
            password = TestUtils.faker.internet().password()
        }
        UserType.BLANK_PASSWORD -> UserEntity().apply {
            email = TestUtils.faker.internet().emailAddress()
            password = ""
        }
        UserType.NOT_EMAIL -> UserEntity().apply {
            email = TestUtils.faker.animal().name()
            password = ""
        }
    }

fun createRandomPostUserEntity() = kr.flab.wiki.core.post.persistence.UserEntity().apply {
    id = TestUtils.faker.name().fullName()
    nickName = TestUtils.faker.funnyName().name()
}

fun createRandomPostEntity(
    writer: User,
    title: String = TestUtils.faker.name().title(),
    text: String = TestUtils.faker.lorem().fixedString(1000),
    version: Long = 0,
    createdAt: LocalDateTime = LocalDateTime.now()
): PostEntity {
    return PostEntity().apply {
        this.writer = writer
        this.title = title
        this.text = text
        this.version = version
        this.createdAt = createdAt
    }
}

enum class UserType() {
    BLANK_EMAIL,
    BLANK_PASSWORD,
    NOT_EMAIL
}