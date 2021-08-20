package kr.flab.wiki.core.domain.document.impl

import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.DocumentPostResult

data class DocumentPostResultImpl(
    override val postedDocument: Document,
    override val serverDocument: Document
) : DocumentPostResult
