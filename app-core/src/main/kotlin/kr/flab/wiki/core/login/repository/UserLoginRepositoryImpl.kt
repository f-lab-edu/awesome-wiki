package kr.flab.wiki.core.login.repository

import kr.flab.wiki.core.login.persistence.User
import kr.flab.wiki.core.login.persistence.UserEntity

class UserLoginRepositoryImpl : UserLoginRepository {
    private val database : HashMap<String, String> = HashMap()

    override fun isUserExists(email: String, password: String): Boolean {
        val originPassword : String = database[email] ?: return false
        return originPassword == password
    }

    override fun findUserWithIdAndPassword(email: String, password: String): User? {
        if(isUserExists(email, password)){
            return UserEntity().apply {
                this.email = email
                this.password = password
            }
        }
        return null
    }
}
