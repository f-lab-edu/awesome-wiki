package kr.flab.wiki.app.infrastructure

import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.DocumentHistory
import kr.flab.wiki.core.domain.document.repository.DocumentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class MySqlDocumentRepository(
    @Autowired private val jdbcTemplate: JdbcTemplate
) : DocumentRepository {
    companion object {
        val title = Document::title::name.get()
        val body = Document::body::name.get()
        val updatedAt = Document::updatedAt::name.get()
        val version = Document::version::name.get()
    }

    val mapper = RowMapper { rs: ResultSet, _: Int ->
        val user = jdbcTemplate.queryForObject(
            "SELECT * FROM User WHERE userName = ?",
            MySqlUserRepository.getUserMapper(),
            rs.getString(MySqlUserRepository.userName)
        )
        Document.newInstance(
            rs.getString(title),
            rs.getString(body),
            user,
            rs.getTimestamp(updatedAt).toLocalDateTime(),
            rs.getLong(version)
        )
    }

    override fun findByTitle(title: String): Document? {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM ${Document.name} WHERE ${MySqlDocumentRepository.title} = ?", mapper, title
        )
    }

    override fun save(document: Document): Document {
        jdbcTemplate.update(
            "INSERT INTO ${Document.name}" +
                    " (" +
                    "$title, " +
                    "$body, " +
                    "${MySqlUserRepository.userName}, " +
                    "$updatedAt, " +
                    "$version)" +
                    " VALUES (?,?,?,?,?)",
            document.title,
            document.body,
            document.lastContributor.userName,
            document.updatedAt,
            document.version
        )
        return document
    }

    override fun findAllByTitle(title: String): MutableList<Document> {
        return jdbcTemplate.query("SELECT * FROM ${Document.name} WHERE $title = ?", mapper, title)
    }

    override fun findAllHistoryByTitle(title: String): MutableList<DocumentHistory> {
        TODO("Not yet implemented")
    }
}
