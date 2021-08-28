package kr.flab.wiki.core.domain.document.impl

import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.service.DocumentQueryService
import kr.flab.wiki.core.domain.document.repository.DocumentRepository

internal class DocumentQueryServiceImpl(
    private val docsRepo: DocumentRepository,
) : DocumentQueryService {

    override fun findDocumentsByTitle(title: String): List<Document> {
        return docsRepo.findAllByTitle(title)
    }

    override fun getDocumentByTitle(title: String): Document {
        return docsRepo.getByTitle(title)
    }
}
