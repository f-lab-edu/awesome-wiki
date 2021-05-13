package kr.flab.wiki.core.post.repository

import kr.flab.wiki.core.post.persistence.Post

class PostRepositoryImpl : PostRepository {
    override fun save(post: Post): Boolean {
        return true
    }

    override fun isTitleAlreadyExists(title: String): Boolean {
        return false
    }
}