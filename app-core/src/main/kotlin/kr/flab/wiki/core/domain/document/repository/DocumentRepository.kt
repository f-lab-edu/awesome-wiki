package kr.flab.wiki.core.domain.document.repository

import kr.flab.wiki.core.common.exception.document.DocumentNotFoundException
import kr.flab.wiki.core.domain.document.Document

interface DocumentRepository {
    /**
     * Confident operation: 검색 실패시 예외 발생
     */
    @Throws(DocumentNotFoundException::class)
    fun getByTitle(title: String): Document =
        this.findByTitle(title) ?: throw DocumentNotFoundException()

    /**
     * Maybe operation: 검색 실패를 예외 처리하지 않음
     */
    fun findByTitle(title: String): Document?

    fun save(document: Document): Document

    fun findAllByTitle(title: String): MutableList<Document>
}
