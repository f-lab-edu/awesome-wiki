package kr.flab.wiki.core.domain.document.usecases

import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.DocumentPostResult
import kr.flab.wiki.core.domain.document.DocumentService
import kr.flab.wiki.core.domain.document.impl.PostDocumentUseCaseImpl

interface PostDocumentUseCase {
    fun postDocument(document: Document): DocumentPostResult

    companion object {
        fun newInstance(documentService: DocumentService): PostDocumentUseCase {
            return PostDocumentUseCaseImpl(documentService)
        }
    }
}
