package kr.flab.wiki.core.domain.user.impl

import kr.flab.wiki.core.common.exception.user.UserEmailAlreadyExistException
import kr.flab.wiki.core.common.exception.user.UserNameAlreadyExistException
import kr.flab.wiki.core.common.exception.user.WrongUserEmailException
import kr.flab.wiki.core.common.exception.user.WrongUserNameException
import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.domain.user.UserRegistrationPolicy
import kr.flab.wiki.core.domain.user.UserService
import kr.flab.wiki.core.domain.user.persistence.UserEntity
import kr.flab.wiki.core.domain.user.repository.UserRepository
import kr.flab.wiki.lib.time.utcNow

internal class UserServiceImpl(
    private val userRepo: UserRepository,
    private val registrationPolicy: UserRegistrationPolicy,
) : UserService {
    override fun isUserNameExist(userName: String): Boolean {
        return userRepo.findByUserName(userName) != null
    }

    override fun isUserEmailExist(email: String): Boolean {
        return userRepo.findByEmail(email) != null
    }

    override fun registerUser(userName: String, emailAddress: String): User {
        with(this.registrationPolicy) {
            if (!userName.isValidUserName()) {
                throw WrongUserNameException("Invalid user name: $userName")
            }

            if (!emailAddress.isValidEmailAddress()) {
                throw WrongUserEmailException("Invalid email address: $emailAddress")
            }
        }

        if (isUserNameExist(userName)) {
            throw UserNameAlreadyExistException("User with name '$userName' already exists")
        }

        if (isUserEmailExist(emailAddress)) {
            throw UserEmailAlreadyExistException("User with email '$emailAddress' already exists")
        }

        val now = utcNow()
        val user = UserEntity(
            userName = userName,
            emailAddress = emailAddress,
            registeredAt = now,
            lastActiveAt = now
        )

        return this.userRepo.save(user)
    }
}
