package kr.flab.wiki.app.api.document.saveDocument

import kr.flab.wiki.app.api.Path
import kr.flab.wiki.app.type.annotation.ApiHandler
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.DocumentPostResult
import kr.flab.wiki.core.domain.document.usecases.PostDocumentUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = [Path.DOCUMENT], produces = ["application/json"])
@ApiHandler
class SaveDocument(private val postDocumentUseCase: PostDocumentUseCase) {
    @PostMapping
    fun onRequest(@RequestBody document: Document): DocumentPostResult {
        return postDocumentUseCase.postDocument(document)
    }
}
