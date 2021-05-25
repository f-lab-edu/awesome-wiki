package kr.flab.wiki.core.common.exception.user

class UserEmailAlreadyExistException(
    override val message: String = "",
    override val cause: Throwable? = null
) : UserAlreadyExistException(message, cause)
