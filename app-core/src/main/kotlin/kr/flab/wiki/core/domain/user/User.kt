package kr.flab.wiki.core.domain.user

import kr.flab.wiki.core.common.annotation.DomainModel
import kr.flab.wiki.core.domain.user.persistence.UserEntity
import java.time.LocalDateTime

@DomainModel
interface User {
    /**
     * 사용자 식별 수단, 한번 결정하면 다시는 바꿀 수 없음
     */

    val userName: String

    var emailAddress: String

    val registeredAt: LocalDateTime

    var lastActiveAt: LocalDateTime

    companion object {
        val name: String = User::class.java.simpleName
        fun newInstance(
            userName: String,
            emailAddress: String,
            registeredAt: LocalDateTime,
            lastActiveAt: LocalDateTime
        ): User {
            return UserEntity(userName, emailAddress, registeredAt, lastActiveAt)
        }
    }
}
