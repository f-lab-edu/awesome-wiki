package kr.flab.wiki.core.post.repository

import kr.flab.wiki.core.post.PostDatabase
import kr.flab.wiki.core.post.persistence.Post

class PostRepositoryImpl(private val database: PostDatabase) : PostRepository {
    override fun save(post: Post): Post? {
        return if (database.posts.add(post)) post else null
    }

    override fun isTitleAlreadyExists(title: String): Boolean {
        return database.posts.stream().anyMatch { post -> post.title == title }
    }

    override fun getPosts(): MutableList<Post> {
        return database.posts
    }
}