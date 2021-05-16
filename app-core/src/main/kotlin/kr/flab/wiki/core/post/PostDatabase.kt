package kr.flab.wiki.core.post

import kr.flab.wiki.core.post.persistence.Post

data class PostDatabase(val posts: MutableList<Post>)
