package kr.flab.wiki.core.common.exception.document

import kr.flab.wiki.core.common.exception.ValidationException

open class DocumentValidationException(
    override val message: String = "",
    override val cause: Throwable? = null
) : ValidationException(message, cause)
