package kr.flab.wiki.core.login.persistence

import kr.flab.wiki.core.login.LoginUtils
import kr.flab.wiki.core.login.exception.UserValidationException

class UserEntity : User {
    override var email : String = ""
//        set(value) {
//            if(value.isEmpty()){
//                throw UserValidationException("Email is Empty!")
//            }
//            if (!LoginUtils.isValidEmail(value)) {
//                throw UserValidationException("Not Email Type!")
//            }
//            field = value
//        }
    override var password : String = ""
//        set(value) {
//            if (value.isEmpty()) {
//                throw UserValidationException("Password is Empty!")
//            }
//            field = value
//        }
}
