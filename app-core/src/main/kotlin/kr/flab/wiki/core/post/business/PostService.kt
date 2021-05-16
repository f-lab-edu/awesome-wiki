package kr.flab.wiki.core.post.business

import kr.flab.wiki.core.post.persistence.Post

interface PostService {
    fun writePost(post: Post): Post?
    fun getPosts(): List<Post>
    fun getPost(id: Long): Post?
    fun editPost(post: Post): Post?
}