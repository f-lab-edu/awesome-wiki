package kr.flab.wiki.core.common.exception.document

class InvalidTitleException(
    override val message: String = "",
    override val cause: Throwable? = null
) : DocumentValidationException(message, cause)
