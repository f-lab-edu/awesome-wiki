package kr.flab.wiki.core.domain.user

import kr.flab.wiki.core.common.annotation.DomainService
import kr.flab.wiki.core.domain.user.impl.UserRegisterServiceImpl
import kr.flab.wiki.core.domain.user.repository.UserRepository

@DomainService
interface UserRegisterService {

    fun registerUser(userName: String, emailAddress: String): User

    companion object {
        fun newInstance(
            userRepository: UserRepository,
            userRegistrationPolicy: UserRegistrationPolicy = UserRegistrationPolicy.DEFAULT
        ): UserRegisterService = UserRegisterServiceImpl(userRepository, userRegistrationPolicy)
    }
}
