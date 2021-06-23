package kr.flab.wiki.core.domain.document.persistence

import kr.flab.wiki.core.domain.document.DocumentHistory
import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

internal class DocumentHistoryEntity(
    override val title: String,

    override val body: String,

    override val creator: User,

    override val createdAt: LocalDateTime,
) : DocumentHistory
