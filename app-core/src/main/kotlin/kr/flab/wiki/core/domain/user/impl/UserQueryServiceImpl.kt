package kr.flab.wiki.core.domain.user.impl
import kr.flab.wiki.core.domain.user.UserQueryService
import kr.flab.wiki.core.domain.user.repository.UserRepository

internal class UserQueryServiceImpl(
    private val userRepo: UserRepository
) : UserQueryService {
    override fun isUserNameExist(userName: String): Boolean {
        return userRepo.findByUserName(userName) != null
    }

    override fun isUserEmailExist(email: String): Boolean {
        return userRepo.findByEmail(email) != null
    }
}
