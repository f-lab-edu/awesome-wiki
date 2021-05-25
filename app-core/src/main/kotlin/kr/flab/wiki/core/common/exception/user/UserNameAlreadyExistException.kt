package kr.flab.wiki.core.common.exception.user

class UserNameAlreadyExistException(
    override val message: String = "",
    override val cause: Throwable? = null
) : UserAlreadyExistException(message, cause)
