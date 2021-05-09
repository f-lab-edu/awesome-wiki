package kr.flab.wiki.core.domain.post

import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

interface Post {
    val creator: User
    val mainText: String
    val title: String
    val idx: Long
    val version: Long
    val lastModified: LocalDateTime
}