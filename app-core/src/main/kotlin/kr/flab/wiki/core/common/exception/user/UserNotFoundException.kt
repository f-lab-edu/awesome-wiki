package kr.flab.wiki.core.common.exception.user

class UserNotFoundException(
    override val message: String = "",
    override val cause: Throwable? = null
) : Exception(message, cause)
