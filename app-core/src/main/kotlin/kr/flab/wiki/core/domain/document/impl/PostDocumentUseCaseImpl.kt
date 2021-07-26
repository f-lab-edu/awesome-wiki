package kr.flab.wiki.core.domain.document.impl

import kr.flab.wiki.core.common.exception.document.DocumentConflictException
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.DocumentPostResult
import kr.flab.wiki.core.domain.document.DocumentService
import kr.flab.wiki.core.domain.document.usecases.PostDocumentUseCase

class PostDocumentUseCaseImpl(
    private val documentService: DocumentService
) : PostDocumentUseCase {
    override fun postDocument(document: Document): DocumentPostResult {
        var serverDocument = documentService.getDocumentByTitle(document.title)
        if (serverDocument.version != document.version) {
            return DocumentPostResultImpl(document, serverDocument)
        } else {
            return try {
                val updatedDocument = documentService.saveDocument(
                    document.title,
                    document.body,
                    document.lastContributor,
                    document.version
                )
                DocumentPostResultImpl(document, updatedDocument)
            } catch (e: DocumentConflictException) {
                println(e)
                serverDocument = documentService.getDocumentByTitle(document.title)
                DocumentPostResultImpl(document, serverDocument)
            }
        }
    }
}
