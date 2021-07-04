package kr.flab.wiki.core.domain.user.impl

import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.domain.user.usecases.UserLoginUseCase
import kr.flab.wiki.core.domain.user.repository.UserRepository

class UserLoginUseCaseImpl(
    private val userRepository: UserRepository
) : UserLoginUseCase {
    override fun login(email: String, password: String): User? {
        return userRepository.findUserByEmailAndPassword(email, password)
    }
}
