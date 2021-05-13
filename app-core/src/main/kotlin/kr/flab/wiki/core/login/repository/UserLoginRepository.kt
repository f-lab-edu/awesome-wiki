package kr.flab.wiki.core.login.repository

import kr.flab.wiki.core.login.persistence.User

interface UserLoginRepository {
    fun isUserExists(email : String, password : String) : Boolean
    fun findUserWithIdAndPassword(email : String, password : String) : User?
}
