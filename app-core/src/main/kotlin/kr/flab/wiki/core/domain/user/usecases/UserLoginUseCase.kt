package kr.flab.wiki.core.domain.user.usecases

import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.domain.user.impl.UserLoginUseCaseImpl
import kr.flab.wiki.core.domain.user.repository.UserRepository

interface UserLoginUseCase {

    fun login(email: String, password: String): User?

    companion object {
        fun newInstance(
            userRepository: UserRepository
        ): UserLoginUseCase {
            return UserLoginUseCaseImpl(userRepository)
        }
    }
}
