package kr.flab.wiki.core.domain.user

import kr.flab.wiki.core.common.annotation.DomainService
import kr.flab.wiki.core.domain.user.impl.UserQueryServiceImpl
import kr.flab.wiki.core.domain.user.repository.UserRepository

@DomainService
interface UserQueryService {
    fun isUserNameExist(userName: String): Boolean

    fun isUserEmailExist(email: String): Boolean

    companion object {
        fun newInstance(
            userRepository: UserRepository
        ): UserQueryService = UserQueryServiceImpl(userRepository)
    }
}
