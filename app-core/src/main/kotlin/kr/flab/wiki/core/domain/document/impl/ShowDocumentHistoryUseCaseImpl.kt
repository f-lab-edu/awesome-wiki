package kr.flab.wiki.core.domain.document.impl

import kr.flab.wiki.core.domain.document.DocumentHistory
import kr.flab.wiki.core.domain.document.repository.DocumentRepository
import kr.flab.wiki.core.domain.document.usecases.ShowDocumentHistoryUseCase

class ShowDocumentHistoryUseCaseImpl(
    private val documentRepository: DocumentRepository
) : ShowDocumentHistoryUseCase {
    override fun findDocumentHistory(title: String): MutableList<DocumentHistory> {
        return documentRepository.findAllHistoryByTitle(title)
    }
}
