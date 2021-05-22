package kr.flab.wiki.core.testlib.document

import com.github.javafaker.Faker
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.persistence.DocumentEntity
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
            creator = creator,
            createdAt = now,
            updatedAt = now,
            version = 1L
        )
    }

    fun randomDocument(
        title: String? = null,
        body: String? = null,
        creator: User? = null,
        createdAt: LocalDateTime? = null,
        updatedAt: LocalDateTime? = null,
        version: Long? = null
    ): Document {
        val faker = Faker.instance()
        val now = utcNow()

        return DocumentEntity(
            title = title ?: faker.lorem().word(),
            body = body ?: faker.lorem().paragraph(),
            creator = creator ?: Users.randomUser(),
            createdAt = createdAt ?: now,
            updatedAt = updatedAt ?: now,
            version = version ?: faker.number().randomNumber(4, false)
        )
    }
}
