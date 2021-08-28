package kr.flab.wiki.app.api.document.saveDocument

import kr.flab.wiki.app.api.Path
import kr.flab.wiki.app.type.annotation.ApiHandler
import kr.flab.wiki.core.common.exception.document.DocumentConflictException
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.DocumentPostResult
import kr.flab.wiki.core.domain.document.service.DocumentSaveService
import kr.flab.wiki.core.domain.document.service.DocumentQueryService
import kr.flab.wiki.core.domain.document.impl.DocumentPostResultImpl
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = [Path.DOCUMENT], produces = ["application/json"])
@ApiHandler
class SaveDocument(
    private val documentSaveService: DocumentSaveService,
    private val documentQueryService: DocumentQueryService
) {
    @PostMapping
    fun onRequest(@RequestBody document: Document): DocumentPostResult {
        var serverDocument = documentQueryService.getDocumentByTitle(document.title)
        if (serverDocument.version != document.version) {
            return DocumentPostResultImpl(document, serverDocument)
        } else {
            return try {
                val updatedDocument = documentSaveService.saveDocument(
                    document.title,
                    document.body,
                    document.lastContributor,
                    document.version
                )
                DocumentPostResultImpl(document, updatedDocument)
            } catch (e: DocumentConflictException) {
                println(e)
                serverDocument = documentQueryService.getDocumentByTitle(document.title)
                DocumentPostResultImpl(document, serverDocument)
            }
        }
    }
}
