package kr.flab.wiki.core.domain.document.impl

import kr.flab.wiki.core.common.exception.document.InvalidBodyException
import kr.flab.wiki.core.common.exception.document.InvalidTitleException
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.DocumentFormatPolicy
import kr.flab.wiki.core.domain.document.DocumentService
import kr.flab.wiki.core.domain.document.persistence.DocumentEntity
import kr.flab.wiki.core.domain.document.repository.DocumentRepository
import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.lib.time.utcNow

internal class DocumentServiceImpl(
    private val docsRepo: DocumentRepository,
    docsPolicy: DocumentFormatPolicy,
) : DocumentService {
    private val validator = DocumentValidator(docsPolicy)

    override fun saveDocument(title: String, body: String, creator: User): Document {
        if (!this.validator.isTitleValid(title)) {
            throw InvalidTitleException("Cannot create document with title '$title'")
        }

        if (!this.validator.isBodyValid(body)) {
            throw InvalidBodyException("Cannot create document with title '$title'")
        }

        val document = docsRepo.findByTitle(title).let {
            val now = utcNow()
            if (it == null) {
                return@let DocumentEntity(
                    title = title,
                    body = body,
                    creator = creator,
                    createdAt = now,
                    updatedAt = now,
                    version = 1L
                )
            } else {
                // TODO: 작성자 외의 사람도 편집 가능한지? 이걸 정책으로 뺄 수 있을지?

                return it.apply {
                    this.body = body
                    this.updatedAt = now
                    this.version = ++version
                }
            }
        }

        return this.docsRepo.save(document)
    }
}
