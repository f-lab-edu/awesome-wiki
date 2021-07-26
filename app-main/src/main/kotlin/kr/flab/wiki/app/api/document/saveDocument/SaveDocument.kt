package kr.flab.wiki.app.api.document.saveDocument

import kr.flab.wiki.app.api.Path
import kr.flab.wiki.app.type.annotation.ApiHandler
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.DocumentService
import kr.flab.wiki.core.domain.user.User
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping(path = [Path.DOCUMENT], produces = ["application/json"])
@ApiHandler
class SaveDocument(private val documentService: DocumentService) {
    @PostMapping
    fun onRequest(@RequestBody document: Document): Document {
        val user = User.newInstance("user", "email", LocalDateTime.now(), LocalDateTime.now())
        return documentService.saveDocument(document.title, document.body, user, document.version)
    }
}
