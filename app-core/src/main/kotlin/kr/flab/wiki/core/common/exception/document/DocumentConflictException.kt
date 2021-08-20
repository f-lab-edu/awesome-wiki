package kr.flab.wiki.core.common.exception.document

class DocumentConflictException(
    override val message: String = "",
    override val cause: Throwable? = null
) : Exception(message, cause)
