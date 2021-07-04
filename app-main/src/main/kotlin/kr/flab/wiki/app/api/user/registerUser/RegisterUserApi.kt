package kr.flab.wiki.app.api.user.registerUser

import kr.flab.wiki.app.api.Path
import kr.flab.wiki.app.api.user.UserResource
import kr.flab.wiki.app.type.annotation.ApiHandler
import kr.flab.wiki.app.type.annotation.ApiRequest
import kr.flab.wiki.core.domain.user.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping(Path.USER)
@ApiHandler
class RegisterUserApi(
    private val userService: UserService
) {
    @GetMapping(
        path = ["register"],
        produces = ["application/json"])
    fun onRequest(
        @ApiRequest @RequestParam userName: String,
        @ApiRequest @RequestParam emailAddress: String
    ): UserResource {
        val registeredUser = userService.registerUser(userName, emailAddress)

        return UserResource.from(registeredUser)
    }
}
