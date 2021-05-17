package kr.flab.wiki.core.post.persistence

import java.time.LocalDateTime

interface Post {
    var writer: User
    var title: String
    var text: String
    var version: Long
    var createdAt: LocalDateTime
}