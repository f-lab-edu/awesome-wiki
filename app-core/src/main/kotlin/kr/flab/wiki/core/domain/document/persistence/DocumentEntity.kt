package kr.flab.wiki.core.domain.document.persistence

import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

internal class DocumentEntity(
    override val title: String,

    override var body: String,

    override var lastContributor: User,

    override var updatedAt: LocalDateTime,

    override var version: Long,
) : Document
