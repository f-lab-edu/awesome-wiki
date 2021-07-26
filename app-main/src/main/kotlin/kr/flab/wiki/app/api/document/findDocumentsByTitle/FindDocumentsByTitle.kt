package kr.flab.wiki.app.api.document.findDocumentsByTitle

import kr.flab.wiki.app.api.Path
import kr.flab.wiki.app.type.annotation.ApiHandler
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.DocumentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = [Path.DOCUMENT], produces = ["application/json"])
@ApiHandler
class FindDocumentsByTitle(private val documentService: DocumentService) {
    @GetMapping("/find")
    fun onRequest(@RequestParam(value = "title") title: String): List<Document> {
        return documentService.findDocumentsByTitle(title)
    }
}