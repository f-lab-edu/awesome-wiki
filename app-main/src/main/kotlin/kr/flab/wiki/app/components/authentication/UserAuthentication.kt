package kr.flab.wiki.app.components.authentication

import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.domain.user.usecases.UserLoginUseCase
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserAuthentication(
    private val passwordEncoder: PasswordEncoder,
    private val userLoginUseCase: UserLoginUseCase,
) {
    fun authenticateUser(email: String, password: String): User? {
        return userLoginUseCase.login(email, passwordEncoder.encode(password))
    }
}
