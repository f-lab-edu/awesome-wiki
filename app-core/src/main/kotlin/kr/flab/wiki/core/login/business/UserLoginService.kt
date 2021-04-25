package kr.flab.wiki.core.login.business
import kr.flab.wiki.core.login.persistence.User

interface UserLoginService {
    fun login(user:User):User?
    fun register(user:User):User?
}
