package kr.flab.wiki.core.domain.document

import kr.flab.wiki.core.common.annotation.DomainModel
import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

@DomainModel
interface Document {
    // TODO Document 식별자는 title로 하는 건지? unique 식별자는 없는건지?
    val title: String

    var body: String

    var creator: User

    val createdAt: LocalDateTime

    var version: Long
}
