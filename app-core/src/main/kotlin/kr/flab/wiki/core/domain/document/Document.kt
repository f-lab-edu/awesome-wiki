package kr.flab.wiki.core.domain.document

import kr.flab.wiki.core.common.annotation.DomainModel
import kr.flab.wiki.core.domain.document.persistence.DocumentEntity
import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

@DomainModel
interface Document {
    /**
     * title은 Document의 식별자
     */
    val title: String

    var body: String

    var lastContributor: User

    var updatedAt: LocalDateTime

    var version: Long

    companion object {
        val name: String = Document::class.java.simpleName
        fun newInstance(
            title: String,
            body: String,
            lastContributer: User,
            updatedAt: LocalDateTime,
            version: Long
        ): Document {
            return DocumentEntity(title, body, lastContributer, updatedAt, version)
        }
    }
}
