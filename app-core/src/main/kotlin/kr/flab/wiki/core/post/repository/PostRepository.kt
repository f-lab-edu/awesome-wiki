package kr.flab.wiki.core.post.repository

import kr.flab.wiki.core.post.persistence.Post

interface PostRepository {
    fun save(post: Post): Post?
    fun isTitleAlreadyExists(title: String): Boolean
}