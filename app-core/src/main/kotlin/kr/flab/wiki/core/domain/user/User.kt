package kr.flab.wiki.core.domain.user

import kr.flab.wiki.core.common.annotation.DomainModel
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
}
