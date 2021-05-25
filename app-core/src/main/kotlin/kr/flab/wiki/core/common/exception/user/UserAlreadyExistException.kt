package kr.flab.wiki.core.common.exception.user

open class UserAlreadyExistException(
    override val message: String = "",
    override val cause: Throwable? = null
) : Exception(message, cause)
