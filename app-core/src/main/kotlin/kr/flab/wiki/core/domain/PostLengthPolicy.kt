package kr.flab.wiki.core.domain

import kr.flab.wiki.core.domain.post.Post

interface PostLengthPolicy {
    val maxTitleLength: Int
    val maxMainTextLength: Int
    fun Post.validTitle(): Boolean {
        val validTitleLength = title.length < maxTitleLength

        return title.isNotBlank() && validTitleLength
    }

    fun Post.validMainText(): Boolean {
        return mainText.length < maxMainTextLength
    }
}
