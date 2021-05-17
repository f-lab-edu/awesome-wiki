package kr.flab.wiki.core.domain.repository

import kr.flab.wiki.core.domain.post.Post

interface PostRepository {
    fun create(post: Post): Boolean
    fun get(title: String): Post?
}
