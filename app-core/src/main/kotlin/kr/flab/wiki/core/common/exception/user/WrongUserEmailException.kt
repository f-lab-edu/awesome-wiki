package kr.flab.wiki.core.common.exception.user

class WrongUserEmailException(
    override val message: String,
    override val cause: Throwable? = null
) : UserValidationException(message, cause)
