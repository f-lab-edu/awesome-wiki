package kr.flab.wiki.core.domain.document

import kr.flab.wiki.core.common.annotation.DomainService
import kr.flab.wiki.core.domain.document.impl.DocumentSaveServiceImpl
import kr.flab.wiki.core.domain.document.repository.DocumentRepository
import kr.flab.wiki.core.domain.user.User

@DomainService
interface DocumentSaveService {
    fun saveDocument(
        title: String,
        body: String,
        creator: User,
        version: Long
    ): Document

    companion object {
        fun newInstance(
            documentRepository: DocumentRepository,
            documentFormatPolicy: DocumentFormatPolicy = DocumentFormatPolicy.DEFAULT,
        ): DocumentSaveService {
            return DocumentSaveServiceImpl(documentRepository, documentFormatPolicy)
        }
    }
}
