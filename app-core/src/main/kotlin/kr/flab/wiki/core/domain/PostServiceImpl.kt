package kr.flab.wiki.core.domain

import kr.flab.wiki.core.domain.post.Post
import kr.flab.wiki.core.domain.repository.PostRepository

class PostServiceImpl(private val postRepository: PostRepository) : PostService {
    override fun create(post: Post): Boolean {
        if (postRepository.get(post.title) != null) {
            return false;
        }
        return postRepository.create(post)
    }

    override fun get(title: String): Post? {
        return postRepository.get(title)
    }
}