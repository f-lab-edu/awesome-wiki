package kr.flab.wiki.core.domain.user.impl

import kr.flab.wiki.core.common.exception.user.UserEmailAlreadyExistException
import kr.flab.wiki.core.common.exception.user.UserNameAlreadyExistException
import kr.flab.wiki.core.common.exception.user.WrongUserEmailException
import kr.flab.wiki.core.common.exception.user.WrongUserNameException
import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.domain.user.UserRegistrationPolicy
import kr.flab.wiki.core.domain.user.UserRegisterService
import kr.flab.wiki.core.domain.user.persistence.UserEntity
import kr.flab.wiki.core.domain.user.repository.UserRepository
import kr.flab.wiki.lib.time.utcNow

internal class UserRegisterServiceImpl(
    private val userRepo: UserRepository,
    private val registrationPolicy: UserRegistrationPolicy,
) : UserRegisterService {

    override fun registerUser(userName: String, emailAddress: String): User {
        with(this.registrationPolicy) {
            if (!userName.isValidUserName()) {
                throw WrongUserNameException("Invalid user name: $userName")
            }

            if (!emailAddress.isValidEmailAddress()) {
                throw WrongUserEmailException("Invalid email address: $emailAddress")
            }
        }

        if (userRepo.findByUserName(userName) != null) {
            throw UserNameAlreadyExistException("User with name '$userName' already exists")
        }

        if (userRepo.findByEmail(emailAddress) != null) {
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
