package kr.flab.wiki.app.api.user.registerUser

import kr.flab.wiki.app.api.user.UserResource
import kr.flab.wiki.app.type.annotation.ApiHandler
import kr.flab.wiki.app.type.annotation.ApiRequest
import kr.flab.wiki.core.domain.user.UserService

@ApiHandler
class RegisterUserApi(
    private val userService: UserService
) {
    fun onRequest(
        @ApiRequest userName: String,
        @ApiRequest emailAddress: String
    ): UserResource {
        val registeredUser = userService.registerUser(userName, emailAddress)

        return UserResource.from(registeredUser)
    }
}
