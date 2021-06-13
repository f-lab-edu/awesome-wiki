package kr.flab.wiki.core.domain.document

import kr.flab.wiki.core.common.annotation.DomainModel
import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

@DomainModel
interface Document {
    val title: String

    var body: String

    var creator: User

    val createdAt: LocalDateTime

    var version: Long
}
