package kr.flab.wiki.core.domain.document

import kr.flab.wiki.core.common.annotation.DomainService
import kr.flab.wiki.core.domain.document.impl.DocumentServiceImpl
import kr.flab.wiki.core.domain.document.repository.DocumentRepository
import kr.flab.wiki.core.domain.user.User

@DomainService
interface DocumentService {
    fun saveDocument(
        title: String,
        body: String,
        creator: User,
    ): Document

    fun findDocumentsByTitle(
        title: String
    ): List<Document>

    fun getDocumentByTitle(
        title: String
    ): Document

    companion object {
        fun newInstance(
            documentRepository: DocumentRepository,
            documentFormatPolicy: DocumentFormatPolicy = DocumentFormatPolicy.DEFAULT,
        ): DocumentService {
            return DocumentServiceImpl(documentRepository, documentFormatPolicy)
        }
    }
}
