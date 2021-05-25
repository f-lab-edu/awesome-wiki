package kr.flab.wiki.core.common.exception

open class ValidationException(
    override val message: String,
    override val cause: Throwable?
) : Exception(message, cause)
