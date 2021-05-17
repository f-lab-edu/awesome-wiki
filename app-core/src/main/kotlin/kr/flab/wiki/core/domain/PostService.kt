package kr.flab.wiki.core.domain

import kr.flab.wiki.core.domain.post.Post

interface PostService {
    fun create(post: Post): Boolean
    fun get(title: String): Post?
}