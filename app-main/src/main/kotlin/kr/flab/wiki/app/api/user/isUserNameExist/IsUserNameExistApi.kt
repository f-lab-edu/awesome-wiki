package kr.flab.wiki.app.api.user.isUserNameExist

import kr.flab.wiki.app.api.Path
import kr.flab.wiki.app.type.annotation.ApiHandler
import kr.flab.wiki.core.domain.user.UserQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = [Path.USER], produces = ["application/json"])
@ApiHandler
class IsUserNameExistApi(
    private val userQueryService: UserQueryService
) {
    @GetMapping(path = ["name"])
    fun onRequest(@RequestParam(value = "name") userName: String): Boolean {
        return this.userQueryService.isUserNameExist(userName)
    }
}
