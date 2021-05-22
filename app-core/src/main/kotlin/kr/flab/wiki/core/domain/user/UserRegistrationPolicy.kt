package kr.flab.wiki.core.domain.user

import java.util.regex.Pattern

interface UserRegistrationPolicy {
    val minimumUserNameLength: Int
    val maximumUserNameLength: Int
    val validUserNamePattern: Pattern
    val minimumEmailLength: Int
    val maximumEmailLength: Int
    val validEmailPattern: Pattern

    fun String.isValidUserName(): Boolean {
        val isLengthValid = this.length in (minimumUserNameLength + 1) until maximumUserNameLength
        val isPatternValid = validUserNamePattern.matcher(this).matches()

        return isLengthValid && isPatternValid
    }

    fun String.isValidEmailAddress(): Boolean {
        val isLengthValid = this.length in (minimumEmailLength + 1) until maximumEmailLength
        val isPatternValid = validEmailPattern.matcher(this).matches()

        return isLengthValid && isPatternValid
    }

    companion object {
        const val DEFAULT_MINIMUM_USER_NAME_LENGTH = 2
        const val DEFAULT_MAXIMUM_USER_NAME_LENGTH = 16
        const val DEFAULT_MINIMUM_EMAIL_LENGTH = 3
        const val DEFAULT_MAXIMUM_EMAIL_LENGTH = 255

        val DEFAULT_USERNAME_PATTERN: Pattern = Pattern.compile("^[A-Za-z0-9]+")

        // https://atin.tistory.com/425
        val DEFAULT_EMAIL_PATTERN: Pattern = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$")

        val DEFAULT = object : UserRegistrationPolicy {
            override val minimumUserNameLength = DEFAULT_MINIMUM_USER_NAME_LENGTH
            override val maximumUserNameLength = DEFAULT_MAXIMUM_USER_NAME_LENGTH
            override val validUserNamePattern = DEFAULT_USERNAME_PATTERN
            override val minimumEmailLength = DEFAULT_MINIMUM_EMAIL_LENGTH
            override val maximumEmailLength = DEFAULT_MAXIMUM_EMAIL_LENGTH
            override val validEmailPattern = DEFAULT_EMAIL_PATTERN
        }
    }
}
