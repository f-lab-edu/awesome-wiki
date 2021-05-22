package kr.flab.wiki.core.testlib.user

import com.github.javafaker.Faker
import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.domain.user.UserRegistrationPolicy
import kr.flab.wiki.core.domain.user.persistence.UserEntity
import kr.flab.wiki.lib.time.utcNow
import java.time.LocalDateTime

object Users {
    fun randomUser(
        userName: String? = null,
        emailAddress: String? = null,
        registeredAt: LocalDateTime? = null,
        lastActiveAt: LocalDateTime? = null
    ): User {
        val faker = Faker.instance()
        val now = utcNow()

        return UserEntity(
            userName = userName ?: faker.lorem().characters(
                UserRegistrationPolicy.DEFAULT_MINIMUM_USER_NAME_LENGTH + 1,
                UserRegistrationPolicy.DEFAULT_MAXIMUM_USER_NAME_LENGTH - 1
            ),
            emailAddress = emailAddress ?: faker.internet().emailAddress(),
            registeredAt = registeredAt ?: now,
            lastActiveAt = lastActiveAt ?: now,
        )
    }
}
