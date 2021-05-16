package kr.flab.wiki.core.post.persistence

import java.time.LocalDateTime

class PostEntity() : Post {
    override var id: Long = 0
    override lateinit var writer: User
    override lateinit var title: String
    override lateinit var text: String
    override var version: Long = 0
    override lateinit var createdAt: LocalDateTime
}
