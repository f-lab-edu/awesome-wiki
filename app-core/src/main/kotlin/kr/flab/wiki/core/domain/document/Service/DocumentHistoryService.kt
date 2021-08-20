package kr.flab.wiki.core.domain.document.Service

import kr.flab.wiki.core.common.annotation.DomainService
import kr.flab.wiki.core.domain.document.DocumentHistory
import kr.flab.wiki.core.domain.document.impl.DocumentHistoryServiceImpl
import kr.flab.wiki.core.domain.document.repository.DocumentRepository

@DomainService
interface DocumentHistoryService {
    fun findDocumentHistory(title: String): MutableList<DocumentHistory>

    companion object {
        fun newInstance(
            documentRepository: DocumentRepository,
        ): DocumentHistoryService {
            return DocumentHistoryServiceImpl(documentRepository)
        }
    }
}
