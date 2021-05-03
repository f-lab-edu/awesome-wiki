package kr.flab.wiki.core.login.respository

interface SessionRepository {
    fun setAttribute(key: String, username: String)
    fun getKey(username: String): String?
}
