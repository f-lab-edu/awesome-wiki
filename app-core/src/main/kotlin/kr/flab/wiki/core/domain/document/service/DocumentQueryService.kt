package kr.flab.wiki.core.domain.document.service

import kr.flab.wiki.core.common.annotation.DomainService
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.impl.DocumentQueryServiceImpl
import kr.flab.wiki.core.domain.document.repository.DocumentRepository

@DomainService
interface DocumentQueryService {

    fun findDocumentsByTitle(
        title: String
    ): List<Document>

    fun getDocumentByTitle(
        title: String
    ): Document

    companion object {
        fun newInstance(
            documentRepository: DocumentRepository,
        ): DocumentQueryService {
            return DocumentQueryServiceImpl(documentRepository)
        }
    }
}
