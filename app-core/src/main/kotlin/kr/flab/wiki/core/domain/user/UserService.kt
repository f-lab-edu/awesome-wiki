package kr.flab.wiki.core.domain.user

import kr.flab.wiki.core.common.annotation.DomainService
import kr.flab.wiki.core.domain.user.impl.UserServiceImpl
import kr.flab.wiki.core.domain.user.repository.UserRepository

@DomainService
interface UserService {
    fun isUserNameExist(userName: String): Boolean

    fun isUserEmailExist(email: String): Boolean

    fun registerUser(userName: String, emailAddress: String): User

    companion object {
        fun newInstance(
            userRepository: UserRepository,
            userRegistrationPolicy: UserRegistrationPolicy = UserRegistrationPolicy.DEFAULT
        ): UserService = UserServiceImpl(userRepository, userRegistrationPolicy)
    }
}
