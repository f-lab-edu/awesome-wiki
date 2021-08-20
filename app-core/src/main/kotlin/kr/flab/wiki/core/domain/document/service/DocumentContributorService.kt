package kr.flab.wiki.core.domain.document.service

import kr.flab.wiki.core.common.annotation.DomainService
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.impl.DocumentContributorServiceImpl
import kr.flab.wiki.core.domain.document.repository.DocumentRepository
import kr.flab.wiki.core.domain.user.User

@DomainService
interface DocumentContributorService {
    fun findDocumentContributor(title: String): List<User>
    fun findContributedDocument(user: User): List<Document>
    companion object {
        fun newInstance(
            documentRepository: DocumentRepository,
        ): DocumentContributorService {
            return DocumentContributorServiceImpl(documentRepository)
        }
    }
}
