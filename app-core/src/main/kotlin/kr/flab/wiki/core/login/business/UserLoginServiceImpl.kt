package kr.flab.wiki.core.login.business

import kr.flab.wiki.core.login.persistence.User
import kr.flab.wiki.core.login.respository.SessionRepository
import kr.flab.wiki.core.login.respository.UserLoginRepository
import java.security.SecureRandom
import java.util.*

class UserLoginServiceImpl(
    private val userLoginRepository: UserLoginRepository,
    private val sessionRepository: SessionRepository
) : UserLoginService {
    override fun login(user: User): User? {
        if (userLoginRepository.findByIdWithPassword(user)) {
            sessionRepository.setAttribute(username = user.email, key = randomUUID())
            return user
        }
        return null
    }

    override fun register(user: User): User? {
        return userLoginRepository.save(user)
    }

    fun randomUUID(): String {
        return UUID.randomUUID().toString()
    }
}
