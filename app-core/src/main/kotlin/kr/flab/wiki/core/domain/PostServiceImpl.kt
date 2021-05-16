package kr.flab.wiki.core.domain

import kr.flab.wiki.core.domain.post.Post
import kr.flab.wiki.core.domain.repository.PostRepository

class PostServiceImpl(
    private val postRepository: PostRepository,
    private val postLengthPolicy: PostLengthPolicy
) : PostService {

    override fun create(post: Post): Boolean {
        with(postLengthPolicy) {
            if (!post.validMainText() || !post.validTitle() || get(post.title) != null)
                return false
        }

        return postRepository.create(post)
    }

    override fun get(title: String): Post? {
        return postRepository.get(title)
    }


}
