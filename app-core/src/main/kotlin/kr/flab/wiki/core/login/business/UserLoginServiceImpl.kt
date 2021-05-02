package kr.flab.wiki.core.login.business

import kr.flab.wiki.core.login.LoginUtils
import kr.flab.wiki.core.login.exception.UserValidationException
import kr.flab.wiki.core.login.persistence.User
import kr.flab.wiki.core.login.repository.SessionRepository
import kr.flab.wiki.core.login.repository.UserLoginRepository

class UserLoginServiceImpl(
    private val userLoginRepository: UserLoginRepository,
    private val sessionRepository: SessionRepository
) : UserLoginService{
    override fun login(user: User) : User? {

        val email : String = user.email
        val password : String = user.password

        if(email.isEmpty()){
            throw UserValidationException("Email is Empty!")
        }

        //이메일 형식이 아닐 경우
        if (!LoginUtils.isValidEmail(email)) {
            throw UserValidationException("Not Email Type!")
        }

        //비밀번호가 비어있을 경우
        if (password.isEmpty()) {
            throw UserValidationException("Password is Empty!")
        }

        val user : User? = userLoginRepository.findUserWithIdAndPassword(email, password)

        //매치되는 회원이 없을 경우
        user ?: throw UserValidationException("There's No Matched Member!")

        //세션 저장
        if (sessionRepository.setAttribute("userName", email) == null) {
            throw UserValidationException("Session creation failed!")
        }

        return user

    }
}
