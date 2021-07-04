package kr.flab.wiki.core.domain.user.repository

import kr.flab.wiki.core.common.exception.user.UserNotFoundException
import kr.flab.wiki.core.domain.user.User

interface UserRepository {
    /**
     * Confident operation: 검색 실패시 예외 발생
     */
    fun getByUserName(userName: String): User =
        this.findByUserName(userName) ?: throw UserNotFoundException()

    /**
     * Maybe operation: 검색 실패를 예외 처리하지 않음
     */
    fun findByUserName(userName: String): User?

    fun findByEmail(email: String): User?

    fun save(user: User): User

    fun findUserByEmailAndPassword(email: String, password: String): User?
}
