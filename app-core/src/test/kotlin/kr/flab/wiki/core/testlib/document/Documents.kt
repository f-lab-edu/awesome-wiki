package kr.flab.wiki.core.testlib.document

import com.github.javafaker.Faker
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.DocumentHistory
import kr.flab.wiki.core.domain.document.persistence.DocumentEntity
import kr.flab.wiki.core.domain.document.persistence.DocumentHistoryEntity
import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.testlib.user.Users
import kr.flab.wiki.lib.time.utcNow
import java.time.LocalDateTime

object Documents {
    fun newDocument(
        title: String? = null,
        body: String? = null,
        creator: User? = null,
    ): Document {
        val now = utcNow()

        return randomDocument(
            title = title,
            body = body,
            lastContributor = creator,
            updatedAt = now,
            version = 1L
        )
    }

    fun randomDocument(
        title: String? = null,
        body: String? = null,
        lastContributor: User? = null,
        updatedAt: LocalDateTime? = null,
        version: Long? = null
    ): Document {
        val faker = Faker.instance()
        val now = utcNow()

        return DocumentEntity(
            title = title ?: faker.lorem().word(),
            body = body ?: faker.lorem().paragraph(),
            lastContributor = lastContributor ?: Users.randomUser(),
            updatedAt = updatedAt ?: now,
            version = version ?: faker.number().randomNumber(4, false)
        )
    }

    fun randomDocumentHistory(
        title: String? = null,
        body: String? = null,
        creator: User? = null,
        createdAt: LocalDateTime? = null,
    ): DocumentHistory {
        val faker = Faker.instance()
        val now = utcNow()

        return DocumentHistoryEntity(
            title = title ?: faker.lorem().word(),
            body = body ?: faker.lorem().paragraph(),
            creator = creator ?: Users.randomUser(),
            createdAt = createdAt ?: now,
        )
    }
}
