package kr.flab.wiki.core.common.exception.user

import kr.flab.wiki.core.common.exception.ValidationException

open class UserValidationException(
    override val message: String,
    override val cause: Throwable? = null
) : ValidationException(message, cause)
