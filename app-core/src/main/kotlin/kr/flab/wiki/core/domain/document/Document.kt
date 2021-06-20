package kr.flab.wiki.core.domain.document

import kr.flab.wiki.core.common.annotation.DomainModel
import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

@DomainModel
interface Document {
    /**
     * title은 Document의 식별자
     */
    var title: String

    var body: String

    var creator: User

    var createdAt: LocalDateTime

    var version: Long
}
