package kr.flab.wiki.app.api.user.registerUser

import kr.flab.wiki.app.api.Path
import kr.flab.wiki.app.api.user.UserResource
import kr.flab.wiki.app.type.annotation.ApiHandler
import kr.flab.wiki.app.type.annotation.ApiRequest
import kr.flab.wiki.core.domain.user.UserRegisterService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Path.USER, produces = ["application/json"])
@ApiHandler
class RegisterUserApi(
    private val userRegisterService: UserRegisterService
) {
    @GetMapping(
        path = ["register"],
        produces = ["application/json"])
    fun onRequest(
        @ApiRequest @RequestParam userName: String,
        @ApiRequest @RequestParam emailAddress: String
    ): UserResource {
        val registeredUser = userRegisterService.registerUser(userName, emailAddress)

        return UserResource.from(registeredUser)
    }
}
