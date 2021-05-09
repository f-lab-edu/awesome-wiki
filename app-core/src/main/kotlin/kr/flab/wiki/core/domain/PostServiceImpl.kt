package kr.flab.wiki.core.domain

import kr.flab.wiki.core.domain.post.Post
import kr.flab.wiki.core.domain.repository.PostRepository
//변수를 저장하기 위한 config 파일 전 테스트단계를 위해 매직넘버 사용 
@Suppress("MagicNumber")
class PostServiceImpl(private val postRepository: PostRepository) : PostService {
    val maxTitleLength = 100
    val maxMainTextLength = 10000
    override fun create(post: Post): Boolean {

        if (!validTitle(post) && !validMainText(post) && get(post.title) != null)
            return false

        return postRepository.create(post)
    }

    override fun get(title: String): Post? {
        return postRepository.get(title)
    }

    private fun validTitle(post: Post): Boolean {
        val empty = post.title.isNotEmpty()
        val blank = post.title.isNotBlank()
        val length = post.title.length < maxTitleLength
        return empty && blank && length
    }
    private fun validMainText(post: Post): Boolean{
        return post.mainText.length < maxMainTextLength
    }

}
