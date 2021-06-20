package kr.flab.wiki.core.domain.document.usecases

import kr.flab.wiki.core.domain.document.DocumentHistory
import kr.flab.wiki.core.domain.document.impl.ShowDocumentHistoryUseCaseImpl
import kr.flab.wiki.core.domain.document.repository.DocumentRepository

interface ShowDocumentHistoryUseCase {
    fun findDocumentHistory(title : String) : MutableList<DocumentHistory>

    companion object {
        fun newInstance(
           documentRepository: DocumentRepository
        ) : ShowDocumentHistoryUseCase {
            return ShowDocumentHistoryUseCaseImpl(documentRepository)
        }
    }
}
