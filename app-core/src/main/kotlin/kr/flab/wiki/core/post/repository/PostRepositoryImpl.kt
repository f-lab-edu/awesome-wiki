package kr.flab.wiki.core.post.repository

import kr.flab.wiki.core.post.persistence.Post

class PostRepositoryImpl : PostRepository {
    override fun save(post: Post): Post? {
        TODO("Not yet implemented")
    }

    override fun isTitleAlreadyExists(title: String): Boolean {
        TODO("Not yet implemented")
    }
}