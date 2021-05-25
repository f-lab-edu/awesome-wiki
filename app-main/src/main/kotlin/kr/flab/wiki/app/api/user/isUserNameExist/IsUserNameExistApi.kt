package kr.flab.wiki.app.api.user.isUserNameExist

import kr.flab.wiki.app.type.annotation.ApiHandler
import kr.flab.wiki.core.domain.user.UserService

@ApiHandler
class IsUserNameExistApi(
    private val userService: UserService
) {
    fun onRequest(userName: String): Boolean {
        return this.userService.isUserNameExist(userName)
    }
}
