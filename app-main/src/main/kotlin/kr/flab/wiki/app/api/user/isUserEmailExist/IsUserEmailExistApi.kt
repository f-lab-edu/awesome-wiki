package kr.flab.wiki.app.api.user.isUserEmailExist

import kr.flab.wiki.app.type.annotation.ApiHandler
import kr.flab.wiki.core.domain.user.UserService

@ApiHandler
class IsUserEmailExistApi(
    private val userService: UserService
) {
    fun onRequest(emailAddress: String): Boolean {
        return this.userService.isUserEmailExist(emailAddress)
    }
}
