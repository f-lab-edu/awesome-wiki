package kr.flab.wiki.core.domain.user.impl

import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.domain.user.repository.UserRepository

internal class UserRepositoryImpl : UserRepository {
    override fun findByUserName(userName: String): User? {
        TODO("Not yet implemented")
    }

    override fun findByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    override fun save(user: User): User {
        TODO("Not yet implemented")
    }

    override fun findUserByEmailAndPassword(email: String, password: String): User? {
        TODO("Not yet implemented")
    }
}
