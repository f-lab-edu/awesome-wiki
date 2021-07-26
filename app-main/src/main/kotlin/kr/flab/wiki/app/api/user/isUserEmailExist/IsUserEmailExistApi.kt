package kr.flab.wiki.app.api.user.isUserEmailExist

import kr.flab.wiki.app.api.Path
import kr.flab.wiki.app.type.annotation.ApiHandler
import kr.flab.wiki.core.domain.user.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = [Path.USER], produces = ["application/json"])
@ApiHandler
class IsUserEmailExistApi(
    private val userService: UserService
) {
    @GetMapping(path = ["email"])
    fun onRequest(@RequestParam(value = "email") emailAddress: String): Boolean {
        return this.userService.isUserEmailExist(emailAddress)
    }
}
