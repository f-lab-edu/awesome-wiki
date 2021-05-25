package kr.flab.wiki.core.common.exception.document

class DocumentNotFoundException(
    override val message: String = "",
    override val cause: Throwable? = null
) : Exception(message, cause)
