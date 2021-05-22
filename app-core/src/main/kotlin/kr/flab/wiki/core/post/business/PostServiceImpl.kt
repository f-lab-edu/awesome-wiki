package kr.flab.wiki.core.post.business

import kr.flab.wiki.core.post.exception.PostValidationException
import kr.flab.wiki.core.post.persistence.Post
import kr.flab.wiki.core.post.repository.PostRepository

class PostServiceImpl(private val postRepository: PostRepository) : PostService {

    override fun writePost(post: Post): Post? {
        if (post.title.length >= MAX_TITLE_LENGTH) {
            throw PostValidationException("제목은 100자 미만이어야 합니다.")
        }
        if (post.text.length >= MAX_TEXT_LENGTH) {
            throw PostValidationException("내용은 10,000자 미만이어야 합니다.")
        }
        if (postRepository.isTitleAlreadyExists(post.title)) {
            throw PostValidationException("이미 해당 제목으로 생성한 포스트가 존재합니다.")
        }
        return postRepository.save(post)
    }

    companion object {
        private const val MAX_TITLE_LENGTH: Int = 100
        private const val MAX_TEXT_LENGTH: Int = 10000
    }

    override fun getPosts(): List<Post> {
        return postRepository.getPosts()
    }

    override fun getPost(id: Long): Post? {
        return postRepository.getPost(id)
    }

    override fun editPost(post: Post): Post? {
        post.id++
        post.version++
        return postRepository.editPost(post)
    }
}