package kr.flab.wiki.core.domain.post

import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

class PostImpl constructor(
    override val creator: User,
    override val title: String,
    override val mainText: String,
    override val idx: Long,
    override val version: Long,
    override val lastModified: LocalDateTime
) : Post
