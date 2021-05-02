package kr.flab.wiki.core.login

import java.util.regex.Pattern

object LoginUtils {
    fun isValidEmail(email: String?): Boolean {
        val regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
}
