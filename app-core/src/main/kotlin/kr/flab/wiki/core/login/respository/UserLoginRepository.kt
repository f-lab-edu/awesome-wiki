package kr.flab.wiki.core.login.respository

import kr.flab.wiki.core.login.persistence.User

interface UserLoginRepository {
    fun findByIdWithPassword(user: User): Boolean
    fun save(user: User): User?
}
