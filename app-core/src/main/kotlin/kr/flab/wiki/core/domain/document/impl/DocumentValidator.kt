package kr.flab.wiki.core.domain.document.impl

import kr.flab.wiki.core.domain.document.DocumentFormatPolicy

internal class DocumentValidator(
    private val policy: DocumentFormatPolicy
) {
    fun isTitleValid(title: String): Boolean = with(policy) {
        title.length in (minTitleLength + 1) until maxTitleLength
    }

    fun isBodyValid(body: String): Boolean = with(policy) {
        body.length in (minBodyLength + 1) until maxBodyLength
    }
}
