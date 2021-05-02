package kr.flab.wiki.core.login

import java.util.regex.Pattern

object LoginUtils {
    // https://atin.tistory.com/425
    private const val REGEX_EMAIL = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"
    private val emailPattern: Pattern = Pattern.compile(REGEX_EMAIL)

    fun isValidEmail(email: String?): Boolean {
        return emailPattern.matcher(email).matches()
    }
}
