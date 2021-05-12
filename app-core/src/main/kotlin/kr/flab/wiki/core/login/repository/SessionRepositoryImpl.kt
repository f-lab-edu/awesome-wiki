package kr.flab.wiki.core.login.repository

class SessionRepositoryImpl : SessionRepository {
    override fun setAttribute(key: String, username: String): String? {
        return username
    }
}
