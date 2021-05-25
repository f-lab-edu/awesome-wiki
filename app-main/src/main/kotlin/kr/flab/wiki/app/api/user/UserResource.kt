package kr.flab.wiki.app.api.user

import kr.flab.wiki.app.type.annotation.ApiResponse
import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

@ApiResponse
data class UserResource(
    val userName: String,
    val emailAddress: String,
    val registeredAt: LocalDateTime,
    val lastActiveAt: LocalDateTime,
) {
    companion object {
        fun from(src: User) = UserResource(
            userName = src.userName,
            emailAddress = src.emailAddress,
            registeredAt = src.registeredAt,
            lastActiveAt = src.lastActiveAt,
        )
    }
}
