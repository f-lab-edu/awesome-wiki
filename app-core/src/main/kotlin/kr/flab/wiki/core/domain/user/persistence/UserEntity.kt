package kr.flab.wiki.core.domain.user.persistence

import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

internal data class UserEntity(
    override val userName: String,

    override var emailAddress: String,

    override val registeredAt: LocalDateTime,

    override var lastActiveAt: LocalDateTime,
) : User
