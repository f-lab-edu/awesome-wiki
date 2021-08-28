package kr.flab.wiki.core.domain.document.impl

import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.service.DocumentContributorService
import kr.flab.wiki.core.domain.document.repository.DocumentRepository
import kr.flab.wiki.core.domain.user.User

class DocumentContributorServiceImpl(
    private val documentRepository: DocumentRepository
) : DocumentContributorService {
    override fun findDocumentContributor(title: String): List<User> {
        val documents = documentRepository.findAllHistoryByTitle(title)

        return documents.map { it.creator }
    }
    override fun findContributedDocument(user: User): List<Document> {
        return documentRepository.findDocumentsByUser(user)
    }
}
