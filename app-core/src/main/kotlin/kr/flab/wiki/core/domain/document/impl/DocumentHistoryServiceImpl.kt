package kr.flab.wiki.core.domain.document.impl

import kr.flab.wiki.core.domain.document.DocumentHistory
import kr.flab.wiki.core.domain.document.service.DocumentHistoryService
import kr.flab.wiki.core.domain.document.repository.DocumentRepository

class DocumentHistoryServiceImpl(
    private val documentRepository: DocumentRepository
) : DocumentHistoryService {
    override fun findDocumentHistory(title: String): List<DocumentHistory> {
        return documentRepository.findAllHistoryByTitle(title)
    }
}
