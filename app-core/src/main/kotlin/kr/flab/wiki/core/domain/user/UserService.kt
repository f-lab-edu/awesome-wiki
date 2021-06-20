package kr.flab.wiki.core.domain.user

import kr.flab.wiki.core.common.annotation.DomainService
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.repository.UserHistoryRepository
import kr.flab.wiki.core.domain.user.impl.UserServiceImpl
import kr.flab.wiki.core.domain.user.repository.UserRepository
import java.time.LocalDateTime

@DomainService
interface UserService {
    fun isUserNameExist(userName: String): Boolean

    fun isUserEmailExist(email: String): Boolean

    fun registerUser(userName: String, emailAddress: String): User

    fun findUserHistory(user: User, range: ClosedRange<LocalDateTime>): List<Document>
    companion object {
        fun newInstance(
            userRepository: UserRepository,
            userRegistrationPolicy: UserRegistrationPolicy = UserRegistrationPolicy.DEFAULT,
            userHistoryRepository: UserHistoryRepository
        ): UserService = UserServiceImpl(userRepository, userRegistrationPolicy, userHistoryRepository)
    }
}
