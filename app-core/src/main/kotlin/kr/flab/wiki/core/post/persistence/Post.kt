package kr.flab.wiki.core.post.persistence

import java.time.LocalDateTime

interface Post {
    val writer : User
    val title : String
    val text : String
    val version : Long
    val createdAt : LocalDateTime
}