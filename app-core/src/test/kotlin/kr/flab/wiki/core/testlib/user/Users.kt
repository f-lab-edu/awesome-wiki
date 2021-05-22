package kr.flab.wiki.core.testlib.user

import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.domain.user.UserRegistrationPolicy
import kr.flab.wiki.core.domain.user.persistence.UserEntity
import kr.flab.wiki.core.login.TestUtils.faker
import kr.flab.wiki.lib.time.utcNow
import java.time.LocalDateTime

object Users {
    fun randomUser(
        userName: String = faker.lorem().characters(
            UserRegistrationPolicy.DEFAULT_MINIMUM_USER_NAME_LENGTH + 1,
            UserRegistrationPolicy.DEFAULT_MAXIMUM_USER_NAME_LENGTH - 1
        ),
        emailAddress: String = faker.internet().emailAddress(),
        registeredAt: LocalDateTime? = null,
        lastActiveAt: LocalDateTime? = null
    ): User {
        val now = utcNow()

        return UserEntity(
            userName = userName,
            emailAddress = emailAddress,
            registeredAt = registeredAt ?: now,
            lastActiveAt = lastActiveAt ?: now,
        )
    }
}
