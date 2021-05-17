package kr.flab.wiki.core.domain.user

import java.time.LocalDateTime

class UserImpl(
    override val name: String,
    override val idx: Long,
    override val lastActiveTime: LocalDateTime
) : User