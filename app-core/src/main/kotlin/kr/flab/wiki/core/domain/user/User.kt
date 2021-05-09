package kr.flab.wiki.core.domain.user

import java.time.LocalDateTime

interface User {
    val name: String
    val idx: Long
    val lastActiveTime: LocalDateTime
}