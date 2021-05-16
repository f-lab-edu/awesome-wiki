package kr.flab.wiki.core.domain

class PostLengthPolicyImpl(
    override val maxTitleLength: Int,
    override val maxMainTextLength: Int) : PostLengthPolicy
